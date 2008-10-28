package sneer.skin.sound.impl;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.threadpool.ThreadPool;
import wheel.io.Logger;

class Recorder implements Runnable {
	
	private boolean _stopCapture = true;
	
	private final int _delay;
	private final AudioFormat _audioFormat;
	private ByteArrayOutputStream _buffer;
	
	@Inject
	static private ThreadPool _threads;
	
	Recorder(AudioFormat audioFormat, ByteArrayOutputStream buffer, int delay) {
		_audioFormat = audioFormat;
		_buffer = buffer;
		_delay = delay;
	}

	@Override
	public void run() {
		Logger.log("Start Record!");
		TargetDataLine targetDataLine = initDataLine();

		_stopCapture = false;
		while (!_stopCapture) {
			appendBytesInBuffer(targetDataLine);
		}

		Logger.log("Stop Record!");
		finalizeDataLine(targetDataLine);
	}

	private TargetDataLine initDataLine() {
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, _audioFormat);
		TargetDataLine targetDataLine = null;
		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(_audioFormat);
		} catch (LineUnavailableException e) {
			if (targetDataLine != null)
				targetDataLine.close();
			throw new IllegalStateException("Can't Play!", e);
		}
		targetDataLine.start();
		return targetDataLine;
	}

	private void appendBytesInBuffer(TargetDataLine targetDataLine) {
		byte tmpArray[] = new byte[_delay];
		int cnt = targetDataLine.read(tmpArray, 0, tmpArray.length);
		if (cnt > 0) {
			_buffer.write(tmpArray, 0, cnt);
			Logger.log(_buffer.size() + " bytes recorded...");
		}
	}

	private void finalizeDataLine(TargetDataLine dataLine) {
		try {
			dataLine.stop();
			dataLine.drain();
			dataLine.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void startRecorder() {
		_stopCapture = false;
		_threads.registerActor(this);
	}

	void stopRecorder() {
		_stopCapture = true;
	}

	public void startRecorder(ByteArrayOutputStream buffer) {
		_buffer = buffer;
		startRecorder();
	}
}