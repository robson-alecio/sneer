package spikes.wheel.testutil;

import static java.lang.System.gc;
import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.Logger;
import spikes.klaus.Daemon;


@SuppressWarnings("deprecation")
public class MemorySentinel {

	private static final int PERIOD_IN_MILLIS = 2000;
	static private boolean _isRunning = false;
	
	private static long _lastUsedMBs = 0;
	
	synchronized public static void startLoggingSignificantMemoryUsageChanges() {
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
		logAnySignificantMemoryUsageChange();
		my(Threads.class).sleepWithoutInterruptions(PERIOD_IN_MILLIS);
	}


	private static void logAnySignificantMemoryUsageChange() {
		if (!isSignificant()) return;
		gc();
		if (!isSignificant()) return;
		_lastUsedMBs = usedMBs();
		my(Logger.class).log("=== MEMORY USED: {} MB", _lastUsedMBs);
	}


	private static boolean isSignificant() {
		return usedMBs() - _lastUsedMBs != 0;
	}


	private static long usedMBs() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
	}


	
}
