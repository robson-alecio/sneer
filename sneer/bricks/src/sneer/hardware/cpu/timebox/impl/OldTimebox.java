package sneer.hardware.cpu.timebox.impl;

import static sneer.commons.environments.Environments.my;

import java.util.HashSet;
import java.util.Set;

import sneer.pulp.logging.Logger;
import wheel.lang.Threads;
import wheel.lang.exceptions.TimeIsUp;

abstract class OldTimebox implements Runnable { //REFACTOR Clean this up. See callers.

	static private final int PRECISION_IN_MILLIS = 500;
	static private final OldTimebox[] ARRAY_TYPE = new OldTimebox[0];
	
	static private final Set<OldTimebox> _activeTimeboxes = java.util.Collections.synchronizedSet(new HashSet<OldTimebox>());
	
	static {
		Thread killer = new Thread("Timebox Killer") { @Override public void run() {
			
			while (true) {
				Threads.sleepWithoutInterruptions(PRECISION_IN_MILLIS);
			
				for (OldTimebox victim : _activeTimeboxes.toArray(ARRAY_TYPE))
					victim.payOrDie(PRECISION_IN_MILLIS);
			}
			
		}};
		killer.setDaemon(true); //Does not use the wheel.lang.Daemon class because all of them are typically killed after each unit test.
		killer.setPriority(Thread.MAX_PRIORITY);
		killer.start();
	}
	
	public OldTimebox(int durationInMillis, boolean runNow) {
		_millisToDie = durationInMillis;
		if (runNow) run();
	}
	
	public OldTimebox(int durationInMillis) {
		this(durationInMillis, true);
	}

	private int _millisToDie = 0;
	private Thread _worker;

	private final Object _isDeadMonitor = new Object();
	private boolean _isDead = false;
	
	private int _suicideAttemptsLeft = 5;
	
	
	protected abstract void runInTimebox();
	
	@Override
	final public void run() {
		try {
			tryToRun();
		} catch (TimeIsUp timeIsUp) {
			diedWith(timeIsUp);
		}
	}

	private void diedWith(TimeIsUp timeIsUp) {
		synchronized (_isDeadMonitor) {
			_isDead = true;
		}
		my(Logger.class).log(timeIsUp);
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
		_activeTimeboxes.remove(this);
	}

	synchronized private void payOrDie(long timeInMillis) {
		if (isDone()) return;

		_millisToDie -= timeInMillis;
		if (_millisToDie <= 0) suicide();
	}

	private boolean isDone() {
		return _worker == null;
	}

	@SuppressWarnings("deprecation")
	private void suicide() {
		synchronized (_isDeadMonitor) {
			if (_isDead) return;
		}

		if (_suicideAttemptsLeft-- == 0) {
			_activeTimeboxes.remove(this);
			threadBlockedNotification(_worker);
			return;
		}
		
		TimeIsUp timeIsUp = new TimeIsUp(_worker.getStackTrace(), "Timebox ended.");
		_worker.stop(timeIsUp);
	}
	

	private void logBlockage(Thread thread) {
		TimeIsUp timeIsUp = new TimeIsUp(thread.getStackTrace(), "Thread running in timebox is blocked waiting for a synchronization monitor and cannot be stopped.");
		diedWith(timeIsUp);
	}


	protected void threadBlockedNotification(Thread thread) {
		logBlockage(thread);
	}

}