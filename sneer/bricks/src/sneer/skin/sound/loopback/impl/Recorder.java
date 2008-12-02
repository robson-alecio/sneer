package sneer.skin.sound.loopback.impl;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.TargetDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.sound.kernel.Audio;

class Recorder {
	
	@Inject static private Audio _audio;
	@Inject static private ThreadPool _threads;
	
	static private ByteArrayOutputStream _buffer;
	static private volatile boolean _isRunning;

	static void stop() {
		_isRunning = false;
	}
	
	static void start(ByteArrayOutputStream buffer) {
		final TargetDataLine targetDataLine = _audio.tryToOpenTargetDataLine();
		if (targetDataLine == null) return;
		
		_buffer = buffer;

		_isRunning = true;
		_threads.registerStepper(new Stepper() { @Override public boolean step() {
			record(targetDataLine);

			if (!_isRunning) {
				targetDataLine.close();
				return false;
			}

			return true;
		}});
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