package sneer.skin.sound.speaker;

import javax.sound.sampled.LineUnavailableException;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;

public interface Speaker extends OldBrick {

	/** Will start playing PcmSoundPacket tuples with duration of one hundredth of a second each. 
	 * @throws LineUnavailableException */
	void open();
	void close();
	Signal<Boolean> isRunning();
}
