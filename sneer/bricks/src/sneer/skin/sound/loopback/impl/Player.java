package sneer.skin.sound.loopback.impl;

import static sneer.commons.environments.Environments.my;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.sound.kernel.Audio;
import wheel.lang.Threads;

class Player {
	
	static private ByteArrayOutputStream _buffer;
	static private volatile boolean _isRunning;
	static private SourceDataLine _sourceDataLine;

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
		my(ThreadPool.class).registerStepper(new Stepper() { @Override public boolean step() {
			playBuffer();

			if (!_isRunning) {
				_sourceDataLine.close();
				return false;
			}

			return true;
		}});
		return true;
	}

	static private void playBuffer() {
		byte[] audioData = readBuffer();
		if (audioData.length == 0) {
			Threads.sleepWithoutInterruptions(100);
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
