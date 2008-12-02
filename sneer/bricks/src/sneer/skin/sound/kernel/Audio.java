package sneer.skin.sound.kernel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import sneer.kernel.container.Brick;

public interface Audio extends Brick {

	AudioFormat defaultAudioFormat();

	TargetDataLine openTargetDataLine() throws LineUnavailableException;
	SourceDataLine tryToOpenSourceDataLine();
	SourceDataLine tryToOpenSourceDataLine(AudioFormat audioFormat);
	

}
