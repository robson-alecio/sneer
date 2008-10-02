package sneer.skin.sound.mic;

import sneer.kernel.container.Brick;


public interface Mic extends Brick {

	/** Will start publishing PcmSoundPacket tuples with duration of one hundredth of a second each. */
	void open();
	void close();

}
