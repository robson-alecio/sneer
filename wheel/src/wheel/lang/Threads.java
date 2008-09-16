package wheel.lang;

import static wheel.io.Logger.log;

import java.util.HashSet;
import java.util.Set;


public class Threads {

	private static final Set<Object> _reactors = new HashSet<Object>();

	public static void waitWithoutInterruptions(Object object) {
		try {
			object.wait();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void sleepWithoutInterruptions(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			log("Unsuccessful attempt to interrupt thread: {}", Thread.currentThread().getName());
		}
	}

	/** @param reactor An object that listens to others (is weak referenced by them), such as a Signal Receiver, and has to react. It does not need a actual thread of its own but it cannot be garbage collected.*/
    public static void preventFromBeingGarbageCollected(Object reactor) {
		_reactors.add(reactor);
	}

	public static void joinWithoutInterruptions(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
		
	}
	
	public static ClassLoader contextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static void startDaemon(String threadName, final Runnable runnable) {
        new Daemon(threadName) { @Override public void run() {
			runnable.run();
		}};
	}

}


