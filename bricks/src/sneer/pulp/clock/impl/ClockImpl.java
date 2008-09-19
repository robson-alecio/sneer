package sneer.pulp.clock.impl;

import java.util.SortedSet;
import java.util.TreeSet;

import sneer.pulp.clock.Clock;
import wheel.lang.Threads;

class ClockImpl implements Clock {
	
	long _currentTimeMillis = 0;
	
	final SortedSet<Alarm> _alarms = new TreeSet<Alarm>();
	
	@Override
	synchronized public void wakeUpNoEarlierThan(long timeToWakeUp, Runnable runnable) {
		int millisFromNow = timeToWakeUp <= _currentTimeMillis
			? 0
			: (int)(timeToWakeUp - _currentTimeMillis);
		wakeUpInAtLeast(millisFromNow, runnable);
	}

	@Override
	synchronized public void wakeUpInAtLeast(int millisFromCurrentTime, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millisFromCurrentTime, false));
		wakeUpAlarmsIfNecessary();
	}

	@Override
	synchronized public void wakeUpEvery(int millisFromCurrentTime, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millisFromCurrentTime, true));
		wakeUpAlarmsIfNecessary();
	}

	@Override
	public void sleepAtLeast(int millis) {
		Runnable notifier = createNotifier();
		synchronized (notifier) {
			wakeUpInAtLeast(millis, notifier);
			Threads.waitWithoutInterruptions(notifier);
		}
	}

	private Runnable createNotifier() {
		return new Runnable() { @Override synchronized public void run() {
			notify();
		}};
	}

	@Override
	synchronized public long time() {
		return _currentTimeMillis;
	}

	@Override
	synchronized public void advanceTime(int deltaMillis) {
		advanceTimeTo(_currentTimeMillis + deltaMillis);
	}

	
	@Override
	synchronized public void advanceTimeTo(long absoluteTimeMillis) {
		_currentTimeMillis = absoluteTimeMillis;
		wakeUpAlarmsIfNecessary();
	}
	
	private void wakeUpAlarmsIfNecessary() {
		while (!_alarms.isEmpty()) {
			Alarm first = _alarms.first();
			if (!first.isTimeToWakeUp()) break;
			
			first.wakeUp();
		}
	}

	private class Alarm implements Comparable<Alarm>{
		
		final int _minimumPeriod;
		
		long _wakeUpTime;
		final Runnable _runnable;

		Alarm(Runnable runnable, int millisFromNow, boolean isPeriodic) {
			if (millisFromNow < 0) throw new IllegalArgumentException();
			_minimumPeriod = isPeriodic ? millisFromNow : 0;
			_wakeUpTime = _currentTimeMillis + millisFromNow;
			_runnable = runnable;
		}
		
		void wakeUp() {
			_alarms.remove(this);
			_runnable.run();

			if (!isPeriodic()) return;
			_wakeUpTime = _currentTimeMillis + _minimumPeriod;
			_alarms.add(this);
		}

		public boolean isPeriodic() {
			return _minimumPeriod>0;
		}

		boolean isTimeToWakeUp() {
			return _currentTimeMillis >= _wakeUpTime;
		}
		
		@Override
		public int compareTo(Alarm alarm) {
			return (int) (_wakeUpTime - alarm._wakeUpTime);
		}
		
		@Override
		public String toString() {
			return "Alarm: " + _wakeUpTime;
		}
	}

}