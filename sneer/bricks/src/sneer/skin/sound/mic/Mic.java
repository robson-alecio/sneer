package sneer.skin.sound.mic;

import sneer.kernel.container.Brick;

public interface Mic extends Brick {

	/** Will start publishing Tuples representing one hundredth of a second of PCM Sound. */
	void open();
	void close();

}
