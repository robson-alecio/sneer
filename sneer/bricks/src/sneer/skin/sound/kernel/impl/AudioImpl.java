package sneer.skin.sound.kernel.impl;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.skin.sound.kernel.Audio;

class AudioImpl implements Audio {

	static private final int SAMPLE_RATE = 8000;
	static private final int SAMPLE_SIZE_IN_BITS = 16;
	static private final int CHANNELS = 1; //If this doesnt work on some linux machines, try 2 channels (stereo).
	static private final boolean SIGNED = true;
	static private final boolean BIG_ENDIAN = false;

	static private final AudioFormat DEFAULT_AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);

	@Inject static private BlinkingLights _lights;
	
	private Light _playbackLight = _lights.prepare(LightType.ERROR);
	private Light _captureLight = _lights.prepare(LightType.ERROR);

	@Override
	public SourceDataLine tryToOpenPlaybackLine() {
		return tryToOpenPlaybackLine(defaultAudioFormat());
	}
	
	@Override
	public SourceDataLine tryToOpenPlaybackLine(AudioFormat audioFormat) {
		SourceDataLine dataLine;
		try {
			dataLine = AudioSystem.getSourceDataLine(audioFormat);
			dataLine.open();
		} catch (LineUnavailableException e) {
			_lights.turnOnIfNecessary(_playbackLight, "Problem with Audio Playback (Speaker)", e);
			return null;
		}
		
		_lights.turnOffIfNecessary(_playbackLight);
		dataLine.start();
		return dataLine;
	}

	
	@Override
	public TargetDataLine tryToOpenCaptureLine() {
		TargetDataLine dataLine;
		try {
			dataLine = AudioSystem	.getTargetDataLine(defaultAudioFormat());
			dataLine.open();
		} catch (LineUnavailableException e) {
			_lights.turnOnIfNecessary(_captureLight, "Problem with Audio Capture (Mic)", e);
			return null;
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
