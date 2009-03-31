package sneer.skin.sound.loopback.impl;

import static sneer.commons.environments.Environments.my;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.sound.kernel.Audio;
class Recorder {
	
	static private ByteArrayOutputStream _buffer;
	static private volatile boolean _isRunning;

	static void stop() {
		_isRunning = false;
	}
	
	static boolean start(ByteArrayOutputStream buffer) {
		final TargetDataLine targetDataLine = tryToOpenCaptureLine();
		if (targetDataLine == null) return false;
		
		_buffer = buffer;

		_isRunning = true;
		my(ThreadPool.class).registerStepper(new Stepper() { @Override public boolean step() {
			record(targetDataLine);

			if (!_isRunning) {
				targetDataLine.close();
				return false;
			}

			return true;
		}});
		return true;
	}

	private static TargetDataLine tryToOpenCaptureLine() {
		try {
			return my(Audio.class).tryToOpenCaptureLine();
		} catch (LineUnavailableException e) {
			return null;
		}
	}

	private static void record(TargetDataLine targetDataLine) {
		byte tmpArray[] = new byte[1024];
		int cnt = targetDataLine.read(tmpArray, 0, tmpArray.length);
		if (cnt == 0) return;

		synchronized (_buffer) {
			_buffer.write(tmpArray, 0, cnt);
		}
	}

}