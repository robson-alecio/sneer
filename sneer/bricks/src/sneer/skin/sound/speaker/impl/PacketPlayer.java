package sneer.skin.sound.speaker.impl;

import javax.sound.sampled.SourceDataLine;

import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import wheel.lang.Consumer;
import static wheel.lang.Environments.my;

class PacketPlayer implements Consumer<PcmSoundPacket> {

	private Audio _audio = my(Audio.class);
	
	private boolean _isRunning = true;
	private SourceDataLine _line;
	
	synchronized void crash() {
		_isRunning = false;
		if (_line == null)	return;
		
		_line.close();
		_line = null;
	}
	
	@Override
	synchronized public void consume(PcmSoundPacket packet) {
		if (!_isRunning) return;
		play(packet);
	}
	
	private void play(PcmSoundPacket packet) {
			ensureLineIsOpen();
			if(_line == null) {
				crash();
				return;
			}

		final byte[] buffer = packet.payload.copy();
		_line.write(buffer, 0, buffer.length);
	}
	
	private void ensureLineIsOpen() {
		if(_line == null)
			_line = _audio.tryToOpenPlaybackLine();
	}
}