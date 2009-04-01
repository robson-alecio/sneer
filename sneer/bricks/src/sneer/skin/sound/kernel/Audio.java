package sneer.skin.sound.kernel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import sneer.brickness.OldBrick;

public interface Audio extends OldBrick {

	AudioFormat defaultAudioFormat();

	TargetDataLine tryToOpenCaptureLine() throws LineUnavailableException;
	SourceDataLine tryToOpenPlaybackLine() throws LineUnavailableException;
	SourceDataLine tryToOpenPlaybackLine(AudioFormat audioFormat) throws LineUnavailableException;
	

}
