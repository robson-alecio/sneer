package sneer.pulp.clock.realtime.mocks;

import sneer.pulp.clock.realtime.RealtimeClock;
import wheel.lang.Threads;

public class RealtimeClockMock implements RealtimeClock {

	private volatile long _currentTime;

	@Override
	public long currentTimeMillis() {
		return _currentTime;
	}
	
	public void advanceTime(int plusTime) {
		_currentTime = _currentTime + plusTime;
		Threads.sleepWithoutInterruptions(500);
	}
	
}