package spikes.sneer.bricks.skin.audio.mic.impl;

import static sneer.foundation.environments.Environments.my;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.foundation.lang.exceptions.FriendlyException;
import spikes.sneer.bricks.skin.audio.kernel.Audio;

class MicLine {

	private static Audio audio() {
		return my(Audio.class);
	}
	
	static private TargetDataLine _delegate;

	private static byte[] _pcmBuffer;
	
	static synchronized void close() {
		if (_delegate == null) return;
		
		_delegate.close();
		_delegate = null;
	}

	static synchronized void tryToAcquire() throws FriendlyException {
		TargetDataLine result = null;
		try {
			result = audio().tryToOpenCaptureLine();
		} catch (LineUnavailableException e) {
			throwFriendly("Unable to find a target data line for your mic.");
		}
		
		_delegate = result;
	}

	static synchronized boolean isAquired() {
		return _delegate != null;
	}

	static ImmutableByteArray read() {
		byte[] buffer = pcmBuffer();
		int read = _delegate.read(buffer , 0, buffer.length);
		return my(ImmutableArrays.class).newImmutableByteArray(buffer, read);
	}

	private static byte[] pcmBuffer() {
		if (_pcmBuffer == null)
			_pcmBuffer = createPcmBuffer();

		return _pcmBuffer;
	}

	private static byte[] createPcmBuffer() {
		return new byte[
            format().getSampleSizeInBits() / 8
            * format().getChannels()
        	* oneFiftiethOfASecond()
    	];
	}

	private static int oneFiftiethOfASecond() {
		return (int) format().getSampleRate() / 50;
	}

	private static AudioFormat format() {
		return audio().defaultAudioFormat();
	}

	private static void throwFriendly(String specifics) throws FriendlyException {
		throw new FriendlyException("Mic not working", specifics + " Try changing your operating system's mic and mixer settings.");
	}
	
}