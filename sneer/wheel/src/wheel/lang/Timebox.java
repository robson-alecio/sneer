package wheel.lang;

import wheel.lang.exceptions.TimeIsUp;

public abstract class Timebox implements Runnable {

	private final int _durationInMillis;

	public Timebox(int durationInMillis, boolean runNow) {
		_durationInMillis = durationInMillis;
		if (runNow) run();
	}
	
	public Timebox(int durationInMillis) {
		this(durationInMillis, true);
	}
	
	protected abstract void runInTimebox();
	
	@Override
	final public void run() {
		final ByRef<Boolean> isDone = ByRef.newInstance(false);
		
		startTimer(isDone);

		runInTimebox();

		synchronized (isDone) {
			isDone.value = true;
		}
	}

	private void startTimer(final ByRef<Boolean> isDone) {
		final Thread worker = Thread.currentThread();

		new Daemon("Timebox") { @Override public void run() {
			sleepFor(_durationInMillis);
			synchronized (isDone) {
				if (!isDone.value) stopThread(worker);
			}
		}}.setPriority(Thread.MAX_PRIORITY);
	}

	@SuppressWarnings("deprecation")
	private void stopThread(Thread thread) {
		thread.stop(new TimeIsUp(thread.getStackTrace()));
	}


	protected void sleepFor(final int millis) {
		Threads.sleepWithoutInterruptions(millis);
	}

}