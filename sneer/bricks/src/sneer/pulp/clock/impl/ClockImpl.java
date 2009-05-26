package sneer.pulp.clock.impl;

import static sneer.commons.environments.Environments.my;

import java.util.SortedSet;
import java.util.TreeSet;

import sneer.commons.lang.ByRef;
import sneer.hardware.cpu.timebox.Timebox;
import sneer.pulp.clock.Clock;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;

class ClockImpl implements Clock {
	
	volatile long _currentTimeMillis = 0;
	
	final SortedSet<Alarm> _alarms = new TreeSet<Alarm>();
	
	final ExceptionHandler _exceptionHandler = my(ExceptionHandler.class);
	
	@Override
	synchronized public void wakeUpNoEarlierThan(long timeToWakeUp, Runnable runnable) {
		long millisFromNow = timeToWakeUp <= _currentTimeMillis
			? 0
			: timeToWakeUp - _currentTimeMillis;
		wakeUpInAtLeast(millisFromNow, runnable);
	}

	@Override
	synchronized public void wakeUpInAtLeast(long millisFromCurrentTime, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millisFromCurrentTime));
	}

	@Override
	synchronized public void wakeUpNowAndEvery(long period, Stepper stepper) {
		if (!step(stepper)) return;
		wakeUpEvery(period, stepper);
	}

	@Override
	synchronized public void wakeUpEvery(long period, Stepper stepper) {
		_alarms.add(new Alarm(stepper, period));
	}

	@Override
	public void sleepAtLeast(long millis) {
		Runnable notifier = my(Threads.class).createNotifier();
		synchronized (notifier) {
			wakeUpInAtLeast(millis, notifier);
			my(Threads.class).waitWithoutInterruptions(notifier);
		}
	}

	@Override
	public long time() {
		return _currentTimeMillis;
	}

	@Override
	synchronized public void advanceTime(long deltaMillis) {
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
		_exceptionHandler.shield(new Runnable() { @Override public void run() {
			my(Timebox.class).run(10000, new Runnable() { @Override public void run() {
				result.value = stepper.step();
			}}, null);
		}});
		return result.value;
	}

	
	static private long _nextSequence = 0;

	private class Alarm implements Comparable<Alarm>{

		final long _sequence = _nextSequence++;
		
		final long _period;
		
		long _wakeUpTime;
		final Stepper _stepper;

		Alarm(final Runnable runnable, long millisFromNow) {
			this(singleStepperFor(runnable), millisFromNow);
		}

		public Alarm(Stepper stepper, long period) {
			if (period < 0) throw new IllegalArgumentException("" + period);
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