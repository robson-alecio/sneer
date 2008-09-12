package sneer.pulp.clock.realtime.impl;

import sneer.pulp.clock.realtime.RealtimeClock;

public class RealtimeClockImpl implements RealtimeClock {

	@Override
	public long currentTimeMillis() {
		return System.currentTimeMillis();
	}

	@Override
	public void setOwner(Runnable owner) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}
	
}
