package sneer.skin.sound.speaker;

import javax.sound.sampled.LineUnavailableException;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;

@Brick
public interface Speaker {

	/** Will start playing PcmSoundPacket tuples with duration of one hundredth of a second each. 
	 * @throws LineUnavailableException */
	void open();
	void close();
	Signal<Boolean> isRunning();
}
