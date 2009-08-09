package sneer.bricks.hardware.cpu.timebox.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.HashSet;
import java.util.Set;

import sneer.bricks.hardware.clock.timer.Timer;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;

@SuppressWarnings("deprecation")
class TimeboxRunner {

	static private final int PRECISION_IN_MILLIS = 500;
	static private final TimeboxRunner[] ARRAY_TYPE = new TimeboxRunner[0];
	static private final Set<TimeboxRunner> _activeTimeboxes = java.util.Collections.synchronizedSet(new HashSet<TimeboxRunner>());

	private final Runnable _toCallWhenBlocked;

	static {
		final Timer timer = my(Timer.class);

		my(Threads.class).startDaemon("Timebox Killer", new Runnable() { @Override public void run() {
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			while (true) {
				timer.sleepAtLeast(PRECISION_IN_MILLIS);

				for (TimeboxRunner victim : _activeTimeboxes.toArray(ARRAY_TYPE))
					victim.payOrDie(PRECISION_IN_MILLIS);
			}			
		}});
	}

	public TimeboxRunner(int durationInMillis, Runnable toRun, Runnable toCallWhenBlocked) {
		_millisToDie = durationInMillis;
		_toCallWhenBlocked = toCallWhenBlocked;
		
		try {
			tryToRun(toRun);
		} catch (TimeIsUp timeIsUp) {
			diedWith(timeIsUp);
		}
	}

	private int _millisToDie = 0;
	private Thread _worker;

	private final Object _isDeadMonitor = new Object();
	private boolean _isDead = false;

	private int _suicideAttemptsLeft = 5;
	
	private void diedWith(TimeIsUp timeIsUp) {
		synchronized (_isDeadMonitor) {
			_isDead = true;
		}
		my(ExceptionLogger.class).log(timeIsUp);
	}

	private void tryToRun(Runnable toRun) {
		try {
			runPre();
			toRun.run();
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

	private void threadBlockedNotification(Thread thread) {
		if (_toCallWhenBlocked != null) {
			_toCallWhenBlocked.run();
			return;
		}
		
		logBlockage(thread);
	}
}