package sneer.bricks.hardware.clock.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.SortedSet;
import java.util.TreeSet;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.threads.OldSteppable;
import sneer.bricks.hardware.cpu.timebox.Timebox;
import sneer.bricks.pulp.exceptionhandling.ExceptionHandler;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.lang.ByRef;

class ClockImpl implements Clock {
	
	private final Register<Long> _currentTimeMillis = my(Signals.class).newRegister(0L);
	
	final SortedSet<Alarm> _alarms = new TreeSet<Alarm>();
	
	final ExceptionHandler _exceptionHandler = my(ExceptionHandler.class);
	
	private long currentTime() {
		return time().currentValue();
	}

	@Override
	public Signal<Long> time() {
		return _currentTimeMillis.output();
	}

	@Override
	synchronized public void advanceTime(long deltaMillis) {
		advanceTimeTo(currentTime() + deltaMillis);
	}

	
	@Override
	synchronized public void advanceTimeTo(long absoluteTimeMillis) {
		_currentTimeMillis.setter().consume(absoluteTimeMillis);
		wakeUpAlarmsIfNecessary();
	}
	
	private void wakeUpAlarmsIfNecessary() {
		while (!_alarms.isEmpty()) {
			Alarm first = _alarms.first();
			if (!first.isTimeToWakeUp()) break;
			
			first.wakeUp();
		}
	}

	
	private boolean step(final OldSteppable stepper) {
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
		final OldSteppable _stepper;

		Alarm(final Runnable runnable, long millisFromNow) {
			this(singleStepperFor(runnable), millisFromNow);
		}

		public Alarm(OldSteppable stepper, long period) {
			if (period < 0) throw new IllegalArgumentException("" + period);
			_period = period;
			_wakeUpTime = currentTime() + period;
			_stepper = stepper;
		}

		void wakeUp() {
			_alarms.remove(this);
			if (!step(_stepper)) return;

			_wakeUpTime = currentTime() + _period;
			_alarms.add(this);
		}

		boolean isTimeToWakeUp() {
			return currentTime() >= _wakeUpTime;
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

	private static OldSteppable singleStepperFor(final Runnable runnable) {
		return new OldSteppable() { @Override public boolean step() {
			runnable.run();
			return false;
		}};
	}

}