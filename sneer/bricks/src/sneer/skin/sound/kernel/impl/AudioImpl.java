package sneer.skin.sound.kernel.impl;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import sneer.skin.sound.kernel.Audio;

class AudioImpl implements Audio {

	@Override
	public SourceDataLine bestAvailableSourceDataLine() {
		return AudioCommon.bestAvailableSourceDataLine();
	}

	@Override
	public TargetDataLine bestAvailableTargetDataLine() {
		return AudioCommon.bestAvailableTargetDataLine();
	}

	@Override
	public AudioFormat audioFormat() {
		return AudioUtil.AUDIO_FORMAT;
	}

	@Override
	public int framesPerAudioPacket() {
		return AudioUtil.FRAMES_PER_AUDIO_PACKET;
	}

	@Override
	public int narrowbandEncoding() {
		return AudioUtil.NARROWBAND_ENCODING; 
	}

	@Override
	public int sampleRate() {
		return AudioUtil.SAMPLE_RATE;
	}

	@Override
	public int sampleSizeInBits() {
		return AudioUtil.SAMPLE_SIZE_IN_BITS;
	}

	@Override
	public boolean signed() {
		return AudioUtil.SIGNED;
	}

	@Override
	public int soundQuality() {
		return AudioUtil.SOUND_QUALITY;
	}

}
