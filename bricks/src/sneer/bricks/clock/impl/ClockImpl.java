package sneer.bricks.clock.impl;

import sneer.bricks.clock.Clock;

public class ClockImpl implements Clock {

	@Override
	public long time() {
		return System.currentTimeMillis();
	}

	@Override
	public void sleep(int millis) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

}
