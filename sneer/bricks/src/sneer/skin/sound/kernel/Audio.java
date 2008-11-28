package sneer.skin.sound.kernel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import sneer.kernel.container.Brick;

public interface Audio extends Brick{

	TargetDataLine openTargetDataLine() throws LineUnavailableException;
	
	SourceDataLine openSourceDataLine() throws LineUnavailableException;
	SourceDataLine openSourceDataLine(AudioFormat audioFormat) throws LineUnavailableException;
	
	AudioFormat audioFormat();
	
	int sampleRate();
	
	boolean signed();
	
	int narrowbandEncoding();
	
	int framesPerAudioPacket();
	
	int soundQuality();
	
	int sampleSizeInBits();
}
