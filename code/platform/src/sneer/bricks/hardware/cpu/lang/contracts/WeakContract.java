package sneer.bricks.hardware.cpu.lang.contracts;

/** A handle for a service that is TERMINATED when this handle is garbage collected. */
public interface WeakContract extends Disposable {
	
	/** Terminates the service immediately (does not wait for this Contract to be garbage collected). */
	void dispose();
	
}
