package wheel.lang;

import java.util.HashSet;
import java.util.Set;

import wheel.io.Logger;
import wheel.lang.exceptions.TimeIsUp;

public abstract class Timebox implements Runnable {

	private static final int PRECISION_IN_MILLIS = 2000;
	private static final Timebox[] ARRAY_TYPE = new Timebox[0];
	
	static private final Set<Timebox> _activeTimeboxes = java.util.Collections.synchronizedSet(new HashSet<Timebox>()); 
	
	static {
		Thread killer = new Thread("Timebox Killer") { @Override public void run() {
			
			while (true) {
				Threads.sleepWithoutInterruptions(PRECISION_IN_MILLIS);
			
				long now = System.currentTimeMillis();
				for (Timebox victim : _activeTimeboxes.toArray(ARRAY_TYPE))
					victim.dieIfDue(now);
			}
			
		}};
		killer.setDaemon(true); //Does not use the wheel.lang.Daemon class because all of them are typically killed after each unit test.
		killer.setPriority(Thread.MAX_PRIORITY);
		killer.start();
	}
	
	public Timebox(int durationInMillis, boolean runNow) {
		_durationInMillis = durationInMillis;
		if (runNow) run();
	}
	
	public Timebox(int durationInMillis) {
		this(durationInMillis, true);
	}

	private final int _durationInMillis;
	private long _timeDue = 0;
	private Thread _worker;
	private boolean _isBlockStatusLogged = false;
	
	
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
			runPre(System.currentTimeMillis());
			runInTimebox();
		} finally {
			runPost();
		}
	}

	synchronized private void runPre(long now) {
		if (_timeDue != 0) throw new IllegalStateException("Timebox was already running.");
	
		_timeDue = now + _durationInMillis;
		_worker = Thread.currentThread();
		_activeTimeboxes.add(this);
	}

	synchronized private void runPost() {
		_timeDue = 0;
		_worker = null;
		_isBlockStatusLogged = false;
		_activeTimeboxes.remove(this);
	}

	synchronized private void dieIfDue(long now) {
		if (isDone()) return;
		if (now < _timeDue) return;
		
		tryToStopThread(_worker);
	}

	private boolean isDone() {
		return _timeDue == 0;
	}

	@SuppressWarnings("deprecation")
	private void tryToStopThread(Thread thread) {
		if (dealWithBlocked(thread)) return;			
		
		TimeIsUp timeIsUp = new TimeIsUp(thread.getStackTrace());
		thread.stop(timeIsUp);
	}
	
	private boolean dealWithBlocked(Thread thread) {
		if (!isBlocked(thread)) return false;
		if (!_isBlockStatusLogged) {
			_isBlockStatusLogged = true;
			logBlockedStatus(thread);
		}
		return true;
	}

	private void logBlockedStatus(Thread thread) {
		TimeIsUp timeIsUp = new TimeIsUp(thread.getStackTrace());
		Logger.log(timeIsUp, "Thread running in timebox is blocked waiting for a sunchronization monitor and cannot be stopped.");
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