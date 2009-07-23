package sneer.bricks.hardware.cpu.threads;

public interface Steppable {

	/** Executes a small step and returns in less than one millisecond. */
	void step();
	
}
