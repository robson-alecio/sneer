package sneer.pulp.clock.impl;

import static wheel.lang.Environments.my;

import java.util.SortedSet;
import java.util.TreeSet;

import sneer.pulp.clock.Clock;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.threadpool.Stepper;
import wheel.lang.ByRef;
import wheel.lang.Fallible;
import wheel.lang.Threads;
import wheel.lang.Timebox;

class ClockImpl implements Clock {
	
	volatile long _currentTimeMillis = 0;
	
	final SortedSet<Alarm> _alarms = new TreeSet<Alarm>();
	
	final ExceptionHandler _exceptionHandler = my(ExceptionHandler.class);
	
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
	}

	@Override
	synchronized public void wakeUpNowAndEvery(int period, Stepper stepper) {
		step(stepper);
		wakeUpEvery(period, stepper);
	}

	@Override
	synchronized public void wakeUpEvery(int period, Stepper stepper) {
		_alarms.add(new Alarm(stepper, period));
	}

	@Override
	public void sleepAtLeast(int millis) {
		Runnable notifier = Threads.createNotifier();
		synchronized (notifier) {
			wakeUpInAtLeast(millis, notifier);
			Threads.waitWithoutInterruptions(notifier);
		}
	}

	@Override
	public long time() {
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
		final ByRef<Boolean> result = ByRef.newInstance(false); 
		_exceptionHandler.shield(new Fallible() { @Override public void run() throws Throwable {
			new Timebox(300000) { @Override protected void runInTimebox() {
				result.value = stepper.step();
			}};
		}});
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