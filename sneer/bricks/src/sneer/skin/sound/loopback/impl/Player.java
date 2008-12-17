package sneer.skin.sound.loopback.impl;

import static sneer.skin.sound.loopback.impl.Services.audio;
import static sneer.skin.sound.loopback.impl.Services.threads;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.SourceDataLine;

import sneer.pulp.threadpool.Stepper;
import wheel.lang.Threads;

class Player {
	
	

	static private ByteArrayOutputStream _buffer;
	static private volatile boolean _isRunning;
	static private SourceDataLine _sourceDataLine;

	static void stop() {
		_isRunning = false;
	}

	static boolean start(ByteArrayOutputStream buffer) {
		_sourceDataLine = audio().tryToOpenPlaybackLine();
		if (_sourceDataLine == null) return false;

		_buffer = buffer;
		
		_isRunning = true;
		threads().registerStepper(new Stepper() { @Override public boolean step() {
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
