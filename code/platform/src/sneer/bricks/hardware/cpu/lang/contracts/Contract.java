package sneer.bricks.hardware.cpu.lang.contracts;

/** A handle for a service that can be terminated when dispose() is called. */
public interface Contract extends Disposable {
	
	/** Terminates the service. */
	void dispose();
	
}
