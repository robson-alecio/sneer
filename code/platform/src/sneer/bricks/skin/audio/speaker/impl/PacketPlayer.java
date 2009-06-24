package sneer.bricks.skin.audio.speaker.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.bricks.skin.audio.PcmSoundPacket;
import sneer.bricks.skin.audio.kernel.Audio;
import sneer.foundation.brickness.PublicKey;
import sneer.foundation.lang.Consumer;

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
		PublicKey publisher = packet.publisher();
		
		if(!_lines.containsKey(publisher))
			_lines.put(publisher, tryToOpenPlaybackLine());

		return _lines.get(publisher);
	}

	private SourceDataLine tryToOpenPlaybackLine() {
		try {
			return _audio.tryToOpenPlaybackLine();
		} catch (LineUnavailableException e) {
			return null;
		}
	}
}