package sneer.skin.sound.mic;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;

@Brick
public interface Mic {

	/** Will start publishing PcmSoundPacket tuples with duration of one hundredth of a second each. */
	void open();
	void close();
	Signal<Boolean> isRunning();
	
}
