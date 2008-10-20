package sneer.skin.sound.kernel.impl;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import sneer.skin.sound.kernel.Audio;

public class AudioImpl implements Audio {

	@Override
	public SourceDataLine bestAvailableSourceDataLine() {
		return AudioCommon.bestAvailableSourceDataLine();
	}

	@Override
	public TargetDataLine bestAvailableTargetDataLine() {
		return AudioCommon.bestAvailableTargetDataLine();
	}

}
