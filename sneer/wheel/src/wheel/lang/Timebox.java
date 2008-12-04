package wheel.lang;

import java.util.HashSet;
import java.util.Set;

import wheel.io.Logger;
import wheel.lang.exceptions.TimeIsUp;

public abstract class Timebox implements Runnable {

	private static final int PRECISION_IN_MILLIS = 500;
	private static final Timebox[] ARRAY_TYPE = new Timebox[0];
	
	static private final Set<Timebox> _activeTimeboxes = java.util.Collections.synchronizedSet(new HashSet<Timebox>()); 
	
	static {
		Thread killer = new Thread("Timebox Killer") { @Override public void run() {
			
			while (true) {
				Threads.sleepWithoutInterruptions(PRECISION_IN_MILLIS);
			
				for (Timebox victim : _activeTimeboxes.toArray(ARRAY_TYPE))
					victim.payOrDie(PRECISION_IN_MILLIS);
			}
			
		}};
		killer.setDaemon(true); //Does not use the wheel.lang.Daemon class because all of them are typically killed after each unit test.
		killer.setPriority(Thread.MAX_PRIORITY);
		killer.start();
	}
	
	public Timebox(int durationInMillis, boolean runNow) {
		_millisToDie = durationInMillis;
		if (runNow) run();
	}
	
	public Timebox(int durationInMillis) {
		this(durationInMillis, true);
	}

	private int _millisToDie = 0;
	private Thread _worker;
	private boolean _isBlockageAlreadyLogged = false;
	
	
	protected abstract void runInTimebox();
	
	@Override
	final public void run() {
		try {
			tryToRun();
		} catch (TimeIsUp timeIsUp) {
			Logger.log(timeIsUp);
		}
	}

	private void tryToRun() {
		try {
			runPre();
			runInTimebox();
		} finally {
			runPost();
		}
	}

	synchronized private void runPre() {
		if (_worker != null) throw new IllegalStateException("Timebox was already running.");
	
		_worker = Thread.currentThread();
		_activeTimeboxes.add(this);
	}

	synchronized private void runPost() {
		_worker = null;
		_isBlockageAlreadyLogged = false;
		_activeTimeboxes.remove(this);
	}

	synchronized private void payOrDie(long timeInMillis) {
		if (isDone()) return;

		_millisToDie -= timeInMillis;
		if (_millisToDie <= 0) die();
	}

	private boolean isDone() {
		return _worker == null;
	}

	@SuppressWarnings("deprecation")
	private void die() {
		if (dealWithBlocked(_worker)) return;			
		
		TimeIsUp timeIsUp = new TimeIsUp(_worker.getStackTrace(), "Timebox ended.");
		_worker.stop(timeIsUp);
	}
	
	private boolean dealWithBlocked(Thread thread) {
		if (!isBlocked(thread)) return false;
		if (!_isBlockageAlreadyLogged) {
			_isBlockageAlreadyLogged = true;
			logBlockage(thread);
		}
		return true;
	}

	private void logBlockage(Thread thread) {
		TimeIsUp timeIsUp = new TimeIsUp(thread.getStackTrace(), "Thread running in timebox is blocked waiting for a synchronization monitor and cannot be stopped.");
		Logger.log(timeIsUp);
	}

	private boolean isBlocked(Thread thread) {
		int tries = 0;
		while (thread.getState() == Thread.State.BLOCKED) {
			if (tries++ == 30) return true;
			Threads.sleepWithoutInterruptions(100);
		}
		return false;
	}

	protected Thread workerThread() {
		return _worker;
	}

}