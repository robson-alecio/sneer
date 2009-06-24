package sneer.bricks.skin.audio.speaker;

import javax.sound.sampled.LineUnavailableException;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface Speaker {

	/** Will start playing PcmSoundPacket tuples with duration of one hundredth of a second each. 
	 * @throws LineUnavailableException */
	void open();
	void close();
	Signal<Boolean> isRunning();
}
