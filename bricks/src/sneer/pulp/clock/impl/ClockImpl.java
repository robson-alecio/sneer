package sneer.pulp.clock.impl;

import java.util.SortedSet;
import java.util.TreeSet;

import sneer.pulp.clock.Clock;
import wheel.lang.Threads;

class ClockImpl implements Clock {
	
	long _currentTimeMillis = 0;
	
	final SortedSet<Alarm> _alarms = new TreeSet<Alarm>();
	
	
	@Override
	synchronized public void addAlarm(int millisFromCurrentTime, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millisFromCurrentTime, false));
	}

	@Override
	synchronized public void addPeriodicAlarm(int millisFromCurrentTime, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millisFromCurrentTime, true));
	}

	@Override
	public void sleep(int millis) {
		Runnable notifier = createNotifier();
		synchronized (notifier) {
			addAlarm(millis, notifier);
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
		
		final int _period;
		
		long _wakeUpTime;
		final Runnable _runnable;

		Alarm(Runnable runnable, int millisFromCurrentTime, boolean isPeriodic) {
			_period = isPeriodic ? millisFromCurrentTime : 0;
			_wakeUpTime = millisFromCurrentTime + _currentTimeMillis;
			_runnable = runnable;
		}
		
		void wakeUp() {
			_runnable.run();

			_alarms.remove(this);

			if (!isPeriodic()) return;
			_wakeUpTime = _wakeUpTime + _period;
			_alarms.add(this);
		}

		public boolean isPeriodic() {
			return _period>0;
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