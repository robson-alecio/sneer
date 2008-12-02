package sneer.skin.sound.kernel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import sneer.kernel.container.Brick;

public interface Audio extends Brick {

	AudioFormat defaultAudioFormat();

	TargetDataLine tryToOpenTargetDataLine();
	SourceDataLine tryToOpenSourceDataLine();
	SourceDataLine tryToOpenSourceDataLine(AudioFormat audioFormat);
	

}
