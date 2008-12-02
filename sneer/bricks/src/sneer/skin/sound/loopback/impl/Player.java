package sneer.skin.sound.loopback.impl;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.sound.kernel.Audio;
import wheel.lang.Threads;

class Player {
	
	@Inject static private ThreadPool _threads;
	@Inject private static Audio _audio;
	@Inject private static BlinkingLights _lights;

	static private Light _light;
	
	static private ByteArrayOutputStream _buffer;
	static private volatile boolean _isRunning;
	static private SourceDataLine _sourceDataLine;

	static void stop() {
		_isRunning = false;
	}

	static void start(ByteArrayOutputStream buffer) {
		if (!tryToOpenLine()) return;

		_buffer = buffer;
		
		_isRunning = true;
		_threads.registerStepper(new Stepper() { @Override public boolean step() {
			playBuffer();

			if (!_isRunning) {
				_sourceDataLine.close();
				return false;
			}

			return true;
		}});
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


	private static boolean tryToOpenLine() {
		_light = _lights.prepare(LightType.ERROR);

		try {
			_sourceDataLine = _audio.openSourceDataLine();
		} catch (LineUnavailableException e) {
			_lights.turnOnIfNecessary(_light, "Problem with Audio Playback", e);
			return false;
		}
		
		_lights.turnOffIfNecessary(_light);
		return true;
	}
}
