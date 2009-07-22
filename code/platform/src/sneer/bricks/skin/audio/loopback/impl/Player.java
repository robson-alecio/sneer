package sneer.bricks.skin.audio.loopback.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.skin.audio.kernel.Audio;

class Player {
	
	static private ByteArrayOutputStream _buffer;
	static private volatile boolean _isRunning;
	static private SourceDataLine _sourceDataLine;
	private static Steppable _refToAvoidGc;

	static void stop() {
		_isRunning = false;
	}

	static boolean start(ByteArrayOutputStream buffer) {
		try {
			_sourceDataLine = my(Audio.class).tryToOpenPlaybackLine();
		} catch (LineUnavailableException e) {
			return false;
		}

		_buffer = buffer;
		
		_isRunning = true;
		_refToAvoidGc = new Steppable() { @Override public boolean step() {
			playBuffer();

			if (!_isRunning) {
				_sourceDataLine.close();
				return false;
			}

			return true;
		}};
		my(Threads.class).newStepper(_refToAvoidGc);
		return true;
	}

	static private void playBuffer() {
		byte[] audioData = readBuffer();
		if (audioData.length == 0) {
			my(Threads.class).sleepWithoutInterruptions(100);
			return;
		}

		_sourceDataLine.write(audioData, 0, audioData.length);
	}


	private static byte[] readBuffer() {
		byte[] result;
		synchronized (_buffer) {
			result = _buffer.toByteArray();
			_buffer.reset();
		}
		return result;
	}

}
