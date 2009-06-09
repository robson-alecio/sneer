package sneer.bricks.pulp.threads;

public interface Stepper {

	/** Returns whether this stepper should be called again. */
	boolean step();
	
}
