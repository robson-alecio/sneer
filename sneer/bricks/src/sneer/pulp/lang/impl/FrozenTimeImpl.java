package sneer.pulp.lang.impl;

import java.util.Date;

import sneer.pulp.lang.FrozenTime;


class FrozenTimeImpl implements FrozenTime {

	private static final ThreadLocal<Long> FROZEN_MILLIS = new ThreadLocal<Long>();

	public Date frozenDate() {
		return new Date(FROZEN_MILLIS.get());
	}

	@Override
	public long frozenTimeMillis() {
		return FROZEN_MILLIS.get();
	}

	@Override
	public void freezeForCurrentThread(long millis) {
		FROZEN_MILLIS.set(millis);
	}
}
