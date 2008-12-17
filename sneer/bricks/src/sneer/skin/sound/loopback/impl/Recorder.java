package sneer.skin.sound.loopback.impl;

import static sneer.skin.sound.loopback.impl.Services.audio;
import static sneer.skin.sound.loopback.impl.Services.threads;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.TargetDataLine;

import sneer.pulp.threadpool.Stepper;
class Recorder {
	
	static private ByteArrayOutputStream _buffer;
	static private volatile boolean _isRunning;

	static void stop() {
		_isRunning = false;
	}
	
	static boolean start(ByteArrayOutputStream buffer) {
		final TargetDataLine targetDataLine = audio().tryToOpenCaptureLine();
		if (targetDataLine == null) return false;
		
		_buffer = buffer;

		_isRunning = true;
		threads().registerStepper(new Stepper() { @Override public boolean step() {
			record(targetDataLine);

			if (!_isRunning) {
				targetDataLine.close();
				return false;
			}

			return true;
		}});
		return true;
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