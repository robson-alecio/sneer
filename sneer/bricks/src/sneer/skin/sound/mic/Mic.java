package sneer.skin.sound.mic;

import sneer.kernel.container.Brick;
import wheel.reactive.Signal;


public interface Mic extends Brick {

	/** Will start publishing PcmSoundPacket tuples with duration of one hundredth of a second each. */
	void open();
	void close();
	Signal<Boolean> isRunning();
	
}
