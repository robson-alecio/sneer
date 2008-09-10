package sneer.pulp.clock.impl;

import sneer.pulp.clock.Alarm;
import sneer.pulp.clock.Clock;
import wheel.lang.Threads;

public class ClockImpl implements Clock {

	@Override
	public long time() {
		return System.currentTimeMillis();
	}

	@Override
	public void sleep(int millis) {
		Threads.sleepWithoutInterruptions(millis);
	}

	@Override
	public Alarm setAlarm(int millis, Runnable runnable) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}
