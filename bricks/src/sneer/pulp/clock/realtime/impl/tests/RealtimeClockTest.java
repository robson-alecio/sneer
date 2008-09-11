package sneer.pulp.clock.realtime.impl.tests;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.clock.realtime.RealtimeClock;

public class RealtimeClockTest extends TestThatIsInjected {
	
	@Inject
	static private RealtimeClock _realtimeClock;
	
	@Test (timeout = 1000)
	public void testRealTime() {
	   while (_realtimeClock.currentTimeMillis() != System.currentTimeMillis());
	}
}