package sneer.skin.sound.mic.impl;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import sneer.kernel.container.Inject;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import wheel.lang.exceptions.FriendlyException;

class MicLine {

	private static final int SAMPLE_RATE_IN_HZ = 8000;
	private static final int SAMPLE_SIZE_IN_BITS = 16;
	private static final int NUMBER_OF_CHANNELS = 2;
	private static final int ONE_HUNDRETH_OF_A_SECOND = SAMPLE_RATE_IN_HZ / 100;

	@Inject
	static private Audio _audio;
	
	static private TargetDataLine _delegate;
	
	static void close() {
		if (_delegate == null) throw new IllegalStateException();
		_delegate.close();
		_delegate = null;
	}

	static void tryToAcquire() throws FriendlyException {
		TargetDataLine result = _audio.bestAvailableTargetDataLine();
		
		if (result == null)
			throwFriendly("Unable to find a target data line for your mic.");
		
		try {
			result.open();
		} catch (LineUnavailableException e) {
			throwFriendly("Unable to open the data line for your mic.");
		}
		
		_delegate = result;
		_delegate.start();
	}

	static boolean isAquired() {
		return _delegate != null;
	}

	static PcmSoundPacket read() {
		byte[] pcmBuffer = new byte[
			SAMPLE_SIZE_IN_BITS / 8 *
			NUMBER_OF_CHANNELS *
			ONE_HUNDRETH_OF_A_SECOND
		];

		int read = _delegate.read(pcmBuffer, 0, pcmBuffer.length);
		return PcmSoundPacket.newInstance(pcmBuffer, read);
	}

	private static void throwFriendly(String specifics) throws FriendlyException {
		throw new FriendlyException("Mic not working", specifics + " Try changing your operating system's mic and mixer settings.");
	}

}
