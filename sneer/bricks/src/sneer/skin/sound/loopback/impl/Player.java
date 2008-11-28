package sneer.skin.sound.loopback.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.sound.kernel.Audio;
import wheel.io.Logger;
import wheel.lang.exceptions.FriendlyException;

class Player implements Runnable {
	
	private boolean _stopPlay = true;
	
	@Inject static private ThreadPool _threads;
	@Inject private static Audio _audio;
	@Inject private static BlinkingLights _lights;
	
	private final int _delay;
	private final AudioFormat _audioFormat;
	private final Light _light = _lights.prepare(LightType.ERROR);
	private ByteArrayOutputStream _buffer;

	private SourceDataLine _sourceDataLine;

	Player(AudioFormat audioFormat, int delay) {
		_audioFormat = audioFormat;
		_delay = delay;
	}
	
	@Override
	public void run() {
		Logger.log("Start Play!");
		try {
			_sourceDataLine = _audio.openSourceDataLine(_audioFormat);
		} catch (LineUnavailableException e) {
			_lights.turnOnIfNecessary(_light, 
					new FriendlyException(e, "Error: audio line is unavailable, can't play!", 
			  									  "Get an expert sovereign friend to help you."));
			close();
		}
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
			playBytesFromBuffer(_sourceDataLine, source);
		}

		Logger.log("Stop Play!");
		close();
	}
	
	public synchronized void close() {
		if(_sourceDataLine==null) return;
		
		_sourceDataLine.stop();
		_sourceDataLine.drain();
		_sourceDataLine.close();
		_sourceDataLine = null;
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

	public void stopPlayer() {
		_stopPlay = true;
	}

	public void startPlayer(ByteArrayOutputStream buffer) {
		_buffer = buffer;
		_stopPlay = false;
		_threads.registerActor(this);
	}
}
