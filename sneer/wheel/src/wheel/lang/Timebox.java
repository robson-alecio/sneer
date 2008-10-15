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
		final Thread worker = Thread.currentThread();
		final ByRef<Boolean> isDone = ByRef.newInstance();
		isDone.value = false;
		
		new Daemon("Timebox") { @Override public void run() {
			sleepFor(_durationInMillis);
			synchronized (isDone) {
				if (!isDone.value) stopWorker(worker);
			}
		}};

		runInTimebox();
		synchronized (isDone) {
			isDone.value = true;
		}
	}

	@SuppressWarnings("deprecation")
	private void stopWorker(Thread thread) {
		thread.stop(new TimeIsUp());
	}


	protected void sleepFor(final int millis) {
		Threads.sleepWithoutInterruptions(millis);
	}

}