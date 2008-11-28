package sneer.skin.sound.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.threadpool.ThreadPool;
import wheel.io.Logger;

class Player implements Runnable {
	
	private boolean _stopPlay = true;
	
	private final int _delay;
	private final AudioFormat _audioFormat;
	private ByteArrayOutputStream _buffer;

	@Inject
	static private ThreadPool _threads;

	Player(AudioFormat audioFormat, int delay) {
		_audioFormat = audioFormat;
		_delay = delay;
	}
	
	@Override
	public void run() {
		Logger.log("Start Play!");
		SourceDataLine dataLine = initDataLine();

		_stopPlay = false;
		while (!_stopPlay) {
			byte[] audioData = readBytesAndResetBuffer();
			if (audioData.length == 0) {
				nothigToPlayTrySleep();
				continue;
			}

			Logger.log(audioData.length + " bytes to play");
			AudioInputStream source = new AudioInputStream(
					new ByteArrayInputStream(audioData), _audioFormat,
					audioData.length / _audioFormat.getFrameSize());
			playBytesFromBuffer(dataLine, source);
		}

		Logger.log("Stop Play!");
		finalizeDataLine(dataLine);
	}

	private SourceDataLine initDataLine() {
		SourceDataLine sourceDataLine = null;
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
				_audioFormat);
		try {
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(_audioFormat);
		} catch (LineUnavailableException e) {
			if (sourceDataLine != null)
				sourceDataLine.close();
			throw new IllegalStateException("Can't Play!", e);
		}
		sourceDataLine.start();
		return sourceDataLine;
	}

	private byte[] readBytesAndResetBuffer() {
		byte[] audioData = _buffer.toByteArray();
		_buffer.reset();
		return audioData;
	}

	private void nothigToPlayTrySleep() {
		Logger.log("nothing to play...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); 
		}
	}

	private void playBytesFromBuffer(SourceDataLine dataLine, AudioInputStream _sourceStream) {
		try {
			int cnt;
			byte tmpBytes[] = new byte[_delay];
			while ((cnt = _sourceStream.read(tmpBytes, 0, tmpBytes.length)) != -1) {
				if (cnt > 0)
					dataLine.write(tmpBytes, 0, cnt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void finalizeDataLine(SourceDataLine dataLine) {
		try {
			dataLine.stop();
			dataLine.drain();
			dataLine.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopPlayer() {
		_stopPlay = true;
	}

	public void startPlayer(ByteArrayOutputStream buffer) {
		_buffer = buffer;
		_stopPlay = false;
		_threads.registerActor(this);
	}
}
