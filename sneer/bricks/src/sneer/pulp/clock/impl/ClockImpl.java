package sneer.pulp.clock.impl;

import java.util.SortedSet;
import java.util.TreeSet;

import sneer.pulp.clock.Clock;
import sneer.pulp.threadpool.Stepper;
import wheel.io.Logger;
import wheel.lang.ByRef;
import wheel.lang.Threads;
import wheel.lang.Timebox;
import wheel.lang.exceptions.TimeIsUp;

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
		_alarms.add(new Alarm(runnable, millisFromCurrentTime));
		wakeUpAlarmsIfNecessary();
	}

	@Override
	synchronized public void wakeUpNowAndEvery(int period, Stepper stepper) {
		step(stepper);
		wakeUpEvery(period, stepper);
	}

	@Override
	synchronized public void wakeUpEvery(int period, Stepper stepper) {
		_alarms.add(new Alarm(stepper, period));
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

	
	private boolean step(final Stepper stepper) {
		final ByRef<Boolean> result = ByRef.newInstance(); 
		new Timebox(3000) { @Override protected void runInTimebox() {
			try {
				result.value = stepper.step();
			} catch (TimeIsUp t) {
				Logger.log("Stepper stopped due to timebox expiry: " + stepper);
				result.value = false;
				throw t;
			}
		}};
		return result.value;
	}

	
	static private long _nextSequence = 0;

	private class Alarm implements Comparable<Alarm>{

		final long _sequence = _nextSequence++;
		
		final int _period;
		
		long _wakeUpTime;
		final Stepper _stepper;

		Alarm(final Runnable runnable, int millisFromNow) {
			this(singleStepperFor(runnable), millisFromNow);
		}

		public Alarm(Stepper stepper, int period) {
			if (period < 0) throw new IllegalArgumentException();
			_period = period;
			_wakeUpTime = _currentTimeMillis + period;
			_stepper = stepper;
		}

		void wakeUp() {
			_alarms.remove(this);
			if (!step(_stepper)) return;

			_wakeUpTime = _currentTimeMillis + _period;
			_alarms.add(this);
		}

		boolean isTimeToWakeUp() {
			return _currentTimeMillis >= _wakeUpTime;
		}
		
		@Override
		public int compareTo(Alarm alarm) {
			if (_wakeUpTime == alarm._wakeUpTime)
				return (int)(_sequence - alarm._sequence);
			return (int) (_wakeUpTime - alarm._wakeUpTime);
		}
		
		@Override
		public String toString() {
			return "Alarm: " + _wakeUpTime;
		}
	}

	private static Stepper singleStepperFor(final Runnable runnable) {
		return new Stepper() { @Override public boolean step() {
			runnable.run();
			return false;
		}};
	}

}