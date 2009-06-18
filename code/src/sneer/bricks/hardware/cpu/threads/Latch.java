package sneer.bricks.hardware.cpu.threads;


/** Same as a java.util.concurrent.CountDownLatch.class of 1 and which does not throw InterruptedException. Throws IllegalState instead.
 * @see java.util.concurrent.CountDownLatch.class */
public interface Latch extends Runnable {

	/** See await() */
	void trip();
	
	/** Waits for some other thread to trip() this latch. If this latch has already been tripped, returns immediately. */
	void await();

	/** Trips this latch. */
	@Override
	void run();
	
}
