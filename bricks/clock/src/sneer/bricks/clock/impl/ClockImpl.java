package sneer.bricks.clock.impl;

import sneer.bricks.clock.Clock;

public class ClockImpl implements Clock {

	@Override
	public long time() {
		return System.currentTimeMillis();
	}

}
