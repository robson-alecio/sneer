package sneer.bricks.hardware.cpu.lang.contract;

/** A handle for a contract that is TERMINATED if this handle is garbage collected. */
public interface Contract {
	
	/** Terminates the contract immediately (does not wait for it to be garbage collected). */
	void dispose();
	
}
