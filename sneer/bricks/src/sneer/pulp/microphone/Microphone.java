package sneer.pulp.microphone;

import sneer.kernel.container.SynchronizedBrick;

public interface Microphone extends SynchronizedBrick {


	/** Will start publishing Tuples representing one hundredth of a second or less of PCM Sound. */
	void open();
	void close();

}
