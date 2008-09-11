package sneer.pulp.clock.impl;

import sneer.pulp.clock.Clock;
import wheel.lang.Threads;

class ClockImpl implements Clock {

	@Override
	public long time() {
		return System.currentTimeMillis();
	}

	@Override
	public void sleep(int millis) {
		Threads.sleepWithoutInterruptions(millis);
	}

	@Override
	public void addAlarm(int millisFromNow, Runnable runnable) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}
	
	@Override
	public void addPeriodicAlarm(int millis, Runnable runnable) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}
