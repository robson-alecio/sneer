package sneer.skin.sound.speaker.impl;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.kernel.container.Inject;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import wheel.lang.Omnivore;

class PacketPlayer implements Omnivore<PcmSoundPacket> {

	@Inject private static Audio _audio;

	
	private boolean _isRunning = true;

	private final SourceDataLine _line = _audio.bestAvailableSourceDataLine();
	

	
	synchronized void crash() {
		_isRunning = false;
		_line.close();
	}
	
	
	@Override
	synchronized public void consume(PcmSoundPacket packet) {
		if (!_isRunning) return;
		play(packet);
	}

	
	private void play(PcmSoundPacket packet) {
		ensureLineIsOpen();

		final byte[] buffer = packet.payload.copy();
		_line.write(buffer, 0, buffer.length);
	}

	
	private void ensureLineIsOpen() {
		if (_line.isActive()) return;
		
		try {
			_line.open();
		} catch (LineUnavailableException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
		_line.start();
	}
	

}
