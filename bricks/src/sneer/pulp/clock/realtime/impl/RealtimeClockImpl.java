package sneer.pulp.clock.realtime.impl;

import sneer.pulp.clock.realtime.RealtimeClock;

class RealtimeClockImpl implements RealtimeClock {

	@Override
	public long currentTimeMillis() {
		return System.currentTimeMillis();
	}

}
