package wheel.lang;

import wheel.lang.exceptions.TimeIsUp;

public abstract class Timebox implements Runnable {

	public Timebox(final int millis) {
		_worker = Thread.currentThread();

		new Daemon("Timebox") { @Override public void run() {
			sleepFor(millis);
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
		_worker.stop(new TimeIsUp());
	}

	protected void sleepFor(final int millis) {
		Threads.sleepWithoutInterruptions(millis);
	}

}