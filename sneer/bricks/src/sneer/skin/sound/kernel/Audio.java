package sneer.skin.sound.kernel;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public interface Audio {

	TargetDataLine bestAvailableTargetDataLine();
	
	SourceDataLine bestAvailableSourceDataLine();

}
