package sneer.bricks.skin.audio.mic;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface Mic {

	/** Will start publishing PcmSoundPacket tuples with duration of one hundredth of a second each. */
	void open();
	void close();
	Signal<Boolean> isRunning();
	
}
