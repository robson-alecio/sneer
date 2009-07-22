package sneer.bricks.skin.audio.loopback.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.skin.audio.kernel.Audio;
class Recorder {
	
	static private ByteArrayOutputStream _buffer;
	static private volatile boolean _isRunning;
	private static Steppable _refToAvoidGc;

	static void stop() {
		_isRunning = false;
	}
	
	static boolean start(ByteArrayOutputStream buffer) {
		final TargetDataLine targetDataLine = tryToOpenCaptureLine();
		if (targetDataLine == null) return false;
		
		_buffer = buffer;

		_isRunning = true;
		_refToAvoidGc = new Steppable() { @Override public boolean step() {
			record(targetDataLine);

			if (!_isRunning) {
				targetDataLine.close();
				return false;
			}

			return true;
		}};
		my(Threads.class).newStepper(_refToAvoidGc);
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