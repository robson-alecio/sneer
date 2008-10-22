package wheel.testutil;

import static java.lang.System.gc;
import wheel.io.Logger;
import wheel.lang.Daemon;
import wheel.lang.Threads;

public class MemorySentinel {

	private static final int PERIOD_IN_MILLIS = 2000;
	static private boolean _isRunning = false;
	
	private static long _usedMBsHigh = 0;
	
	synchronized public static void startLoggingMemoryUsageIncrease() {
		checkAlreadyRunning();
		
		new Daemon(name()) { @Override public void run() {
			while (true) step();
		}};
	}


	private static void checkAlreadyRunning() {
		if (_isRunning) throw new IllegalStateException();
		_isRunning = true;
	}


	private static String name() {
		return MemorySentinel.class.getSimpleName();
	}

	
	static private void step() {
		Threads.sleepWithoutInterruptions(PERIOD_IN_MILLIS);
		long used = usedMBs();
		if (used - _usedMBsHigh <= 0) return;
		gc();
		if (used - _usedMBsHigh <= 0) return;
		_usedMBsHigh = used;
		Logger.log("=== MEMORY USED: {} MB", used);
	}


	private static long usedMBs() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
	}


	
}
