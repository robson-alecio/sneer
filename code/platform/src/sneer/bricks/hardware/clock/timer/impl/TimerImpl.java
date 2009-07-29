package sneer.bricks.hardware.clock.timer.impl;

import static sneer.foundation.environments.Environments.my;

import java.lang.ref.WeakReference;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.clock.timer.Timer;
import sneer.bricks.hardware.cpu.lang.contracts.Contracts;
import sneer.bricks.hardware.cpu.lang.contracts.Disposable;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.cpu.timebox.Timebox;
import sneer.bricks.pulp.exceptionhandling.ExceptionHandler;
import sneer.foundation.lang.Consumer;

class TimerImpl implements Timer {
	
	private final  Clock _clock = my(Clock.class);
	private final SortedSet<Alarm> _alarms = new TreeSet<Alarm>();
	private final  ExceptionHandler _exceptionHandler = my(ExceptionHandler.class);
	
	@SuppressWarnings("unused") private final WeakContract _timeContract;
	
	
	TimerImpl(){
		_timeContract = _clock.time().addReceiver(new Consumer<Long>(){ @Override public void consume(Long value) {
			wakeUpAlarmsIfNecessary();
		}});
	}
	
	
	@Override
	synchronized public WeakContract wakeUpNoEarlierThan(long timeToWakeUp, Runnable runnable) {
		long millisFromNow = timeToWakeUp <= currentTime()
			? 0
			: timeToWakeUp - currentTime();
		return wakeUpInAtLeast(millisFromNow, runnable);
	}

	@Override
	synchronized public WeakContract wakeUpInAtLeast(long millisFromCurrentTime, Runnable runnable) {
		return wakeUp(millisFromCurrentTime, asSteppable(runnable), false);
	}

	@Override
	synchronized public WeakContract wakeUpNowAndEvery(long period, Steppable stepper) {
		step(stepper);
		return wakeUpEvery(period, stepper);
	}

	@Override
	synchronized public WeakContract wakeUpEvery(long period, Steppable stepper) {
		return wakeUp(period, stepper, true);
	}

	private WeakContract wakeUp(long period, Steppable stepper, boolean isPeriodic) {
		Alarm result = new Alarm(stepper, period, isPeriodic);
		_alarms.add(result);
		return my(Contracts.class).weakContractFor(result, stepper);
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

	
	private void step(final Steppable stepper) {
		_exceptionHandler.shield(new Runnable() { @Override public void run() {
			my(Timebox.class).run(10000, new Runnable() { @Override public void run() {
				stepper.step();
			}}, null);
		}});
	}

	
	static private long _nextSequence = 0;

	private class Alarm implements Comparable<Alarm>, Disposable {

		private final WeakReference<Steppable> _stepper;

		private final boolean _isPeriodic;
		private final long _period;
		private long _wakeUpTime;
		private final long _sequence = _nextSequence++;

		volatile
		private boolean _isDisposed = false;

		
		public Alarm(Steppable stepper, long period, boolean isPeriodic) {
			if (period < 0) throw new IllegalArgumentException("" + period);
			_stepper = new WeakReference<Steppable>(stepper);
			_period = period;
			_wakeUpTime = currentTime() + period;
			_isPeriodic = isPeriodic;
		}

		
		void wakeUp() {
			_alarms.remove(this);

			Steppable stepper = _stepper.get();
			if (stepper == null) return;
			if (_isDisposed) return;
			
			step(stepper);

			if (!_isPeriodic) return;
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

		@Override
		public void dispose() {
			_isDisposed = true;
		}

	}

	private static Steppable asSteppable(final Runnable runnable) {
		return new Steppable() { @Override public void step() {
			runnable.run();
		}};
	}

	
	private long currentTime() {
		return _clock.time().currentValue();
	}


}