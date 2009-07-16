package sneer.bricks.hardware.cpu.threads;


/** Same as a java.util.concurrent.CountDownLatch.class of 1 and which does not throw InterruptedException. Throws IllegalState instead.
 * @see java.util.concurrent.CountDownLatch.class */
public interface Latch extends Runnable {

	/** See await() */
	void open();
	
	/** Waits for some other thread to open() this latch. If this latch has already been opened, returns immediately. */
	void await();

	/** Opens this latch. */
	@Override
	void run();
	
}
