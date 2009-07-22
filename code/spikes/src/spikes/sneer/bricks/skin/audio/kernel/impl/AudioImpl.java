package spikes.sneer.bricks.skin.audio.kernel.impl;

import static sneer.foundation.environments.Environments.my;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.blinkinglights.LightType;
import spikes.sneer.bricks.skin.audio.kernel.Audio;

class AudioImpl implements Audio {

	static private final int SAMPLE_RATE = 8000;
	static private final int SAMPLE_SIZE_IN_BITS = 16;
	static private final int CHANNELS = 1; //If this doesnt work on some linux machines, try 2 channels (stereo).
	static private final boolean SIGNED = true;
	static private final boolean BIG_ENDIAN = false;

	static private final AudioFormat DEFAULT_AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);

	private final BlinkingLights _lights = my(BlinkingLights.class);
	
	private Light _playbackLight = _lights.prepare(LightType.ERROR);
	private Light _captureLight = _lights.prepare(LightType.ERROR);

	@Override
	public SourceDataLine tryToOpenPlaybackLine() throws LineUnavailableException {
		return tryToOpenPlaybackLine(defaultAudioFormat());
	}
	
	@Override
	public SourceDataLine tryToOpenPlaybackLine(AudioFormat audioFormat) throws LineUnavailableException {
		SourceDataLine result;
		try {
			result = AudioSystem.getSourceDataLine(audioFormat);
			result.open();
		} catch (LineUnavailableException e) {
			_lights.turnOnIfNecessary(_playbackLight, "Problem with Audio Playback (Speaker)", helpMessageFor("speaker"), e);
			throw e;
		}
		
		_lights.turnOffIfNecessary(_playbackLight);
		result.start();
		return result;
	}

	private String helpMessageFor(String device) {
		return "Some application might be holding the " + device + ". Once the Firefox browser on Linux plays a sound, for example, it holds the sound device forever until you restart it. Try restarting or closing that application and maybe restarting Sneer.";
	}

	
	@Override
	public TargetDataLine tryToOpenCaptureLine() throws LineUnavailableException {
		TargetDataLine dataLine;
		try {
			dataLine = AudioSystem	.getTargetDataLine(defaultAudioFormat());
			dataLine.open();
		} catch (LineUnavailableException e) {
			_lights.turnOnIfNecessary(_captureLight, "Problem with Audio Capture (Mic)", helpMessageFor("mic"), e);
			throw e;
		}

		_lights.turnOffIfNecessary(_captureLight);
		dataLine.start();
		return dataLine;
	}

	@Override
	public AudioFormat defaultAudioFormat() {
		return DEFAULT_AUDIO_FORMAT;
	}

}
