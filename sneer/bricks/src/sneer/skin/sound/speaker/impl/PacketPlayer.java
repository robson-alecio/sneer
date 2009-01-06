package sneer.skin.sound.speaker.impl;

import static wheel.lang.Environments.my;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sound.sampled.SourceDataLine;

import sneer.pulp.keymanager.PublicKey;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import wheel.lang.Consumer;

class PacketPlayer implements Consumer<PcmSoundPacket> {

	private final Audio _audio = my(Audio.class);
	
	private boolean _isRunning = true;
	private final Map<PublicKey, SourceDataLine> _lines = new HashMap<PublicKey, SourceDataLine>();
	
	synchronized void crash() {
		_isRunning = false;
		if (_lines.isEmpty()) return;
		
		Iterator<SourceDataLine> iterator = _lines.values().iterator();
		while (iterator.hasNext()) {
			SourceDataLine line = iterator.next();
			line.close();
			iterator.remove();
		}
	}
	
	@Override
	synchronized public void consume(PcmSoundPacket packet) {
		if (!_isRunning) return;
		play(packet);
	}
	
	private void play(PcmSoundPacket packet) {
		SourceDataLine line = ensureLineIsOpen(packet);
		if (line == null) {
			crash();
			return;
		}

		final byte[] buffer = packet.payload.copy();
		line.write(buffer, 0, buffer.length);
	}
	
	private SourceDataLine ensureLineIsOpen(PcmSoundPacket packet) {
		if(!_lines.containsKey(packet.publisher()))
			_lines.put(packet.publisher(), _audio.tryToOpenPlaybackLine());

		return _lines.get(packet.publisher());
	}
}