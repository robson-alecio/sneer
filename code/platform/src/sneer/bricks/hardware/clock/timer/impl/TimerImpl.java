package sneer.bricks.hardware.clock.timer.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.SortedSet;
import java.util.TreeSet;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.clock.timer.Timer;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.cpu.timebox.Timebox;
import sneer.bricks.pulp.exceptionhandling.ExceptionHandler;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.lang.ByRef;
import sneer.foundation.lang.Consumer;

class TimerImpl implements Timer {
	
	private final  Clock _clock = my(Clock.class);
	private final SortedSet<Alarm> _alarms = new TreeSet<Alarm>();
	private final  ExceptionHandler _exceptionHandler = my(ExceptionHandler.class);
	
	@SuppressWarnings("unused")
	private final  Object _refToAvoidGC;
	
	TimerImpl(){
		_refToAvoidGC = my(Signals.class).receive(_clock.time() , new Consumer<Long>(){ @Override public void consume(Long value) {
			wakeUpAlarmsIfNecessary();
		}});
	}
	
	@Override
	synchronized public void wakeUpNoEarlierThan(long timeToWakeUp, Runnable runnable) {
		long millisFromNow = timeToWakeUp <= currentTime()
			? 0
			: timeToWakeUp - currentTime();
		wakeUpInAtLeast(millisFromNow, runnable);
	}

	private long currentTime() {
		return _clock.time().currentValue();
	}

	@Override
	synchronized public void wakeUpInAtLeast(long millisFromCurrentTime, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millisFromCurrentTime));
	}

	@Override
	synchronized public void wakeUpNowAndEvery(long period, Steppable stepper) {
		if (!step(stepper)) return;
		wakeUpEvery(period, stepper);
	}

	@Override
	synchronized public void wakeUpEvery(long period, Steppable stepper) {
		_alarms.add(new Alarm(stepper, period));
	}

	@Override
	public void sleepAtLeast(long millis) {
		Latch latch = my(Threads.class).newLatch();
		wakeUpInAtLeast(millis, latch);
		latch.waitTillOpen();
	}
	
	private void wakeUpAlarmsIfNecessary() {
		while (!_alarms.isEmpty()) {
			Alarm first = _alarms.first();
			if (!first.isTimeToWakeUp()) break;
			
			first.wakeUp();
		}
	}

	
	private boolean step(final Steppable stepper) {
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
		final Steppable _stepper;

		Alarm(final Runnable runnable, long millisFromNow) {
			this(singleStepperFor(runnable), millisFromNow);
		}

		public Alarm(Steppable stepper, long period) {
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

	private static Steppable singleStepperFor(final Runnable runnable) {
		return new Steppable() { @Override public boolean step() {
			runnable.run();
			return false;
		}};
	}

}