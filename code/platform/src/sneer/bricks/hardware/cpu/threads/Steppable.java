package sneer.bricks.hardware.cpu.threads;

public interface Steppable {

	/** Returns whether this stepper should be called again. */
	boolean step();
	
}
