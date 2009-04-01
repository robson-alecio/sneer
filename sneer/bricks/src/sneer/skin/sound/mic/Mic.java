package sneer.skin.sound.mic;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;


public interface Mic extends OldBrick {

	/** Will start publishing PcmSoundPacket tuples with duration of one hundredth of a second each. */
	void open();
	void close();
	Signal<Boolean> isRunning();
	
}
