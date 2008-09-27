package wheel.testutil;

import wheel.lang.Daemon;
import wheel.lang.Threads;

public abstract class Timebox implements Runnable {

	public Timebox(final int millis) {
		_worker = Thread.currentThread();

		new Daemon("Timebox") { @Override public void run() {
			Threads.sleepWithoutInterruptions(millis);
			timeIsUp();
		}};
		
		run();
		done();
	}

	private final Thread _worker;
	private boolean _isDone = false;

	private synchronized void done() {
		_isDone = true;
	}
	
	@SuppressWarnings("deprecation")
	private synchronized void timeIsUp() {
		if (_isDone) return;
		_worker.stop(new Throwable("Timebox ended."));
	}

}