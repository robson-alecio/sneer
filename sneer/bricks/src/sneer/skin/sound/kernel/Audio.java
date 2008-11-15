package sneer.skin.sound.kernel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public interface Audio {

	TargetDataLine bestAvailableTargetDataLine();
	
	SourceDataLine bestAvailableSourceDataLine();
	
	AudioFormat audioFormat();
	
	int sampleRate();
	
	boolean signed();
	
	int narrowbandEncoding();
	
	int framesPerAudioPacket();
	
	int soundQuality();
	
	int sampleSizeInBits();
}
