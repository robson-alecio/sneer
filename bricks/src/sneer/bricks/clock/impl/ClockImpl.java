package sneer.bricks.clock.impl;

import sneer.bricks.clock.Clock;
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

}
