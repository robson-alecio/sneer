package sneer.pulp.threadpool;

public interface Stepper {

	/** Returns whether this stepper should be called again. */
	boolean step();
	
}
