package sneer.pulp.microphone;

import sneer.kernel.container.Brick;

public interface Microphone extends Brick {

	/** Will start publishing Tuples representing one hundredth of a second of PCM Sound. */
	void open();
	void close();

}
