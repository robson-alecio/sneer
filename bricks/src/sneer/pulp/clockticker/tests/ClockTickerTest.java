package sneer.pulp.clockticker.tests;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.clock.Clock;
import sneer.pulp.clockticker.ClockTicker;
import wheel.lang.Threads;

public class ClockTickerTest extends TestThatIsInjected {

	@Inject
	static private Clock _clock;

	@Inject @SuppressWarnings("unused")
	static private ClockTicker _subject;

	@Test (timeout = 3000)
	public void testTicking() {
		long t0 = _clock.time();
		while (t0 == _clock.time()) {
			System.out.println("Waiting...");
			Threads.sleepWithoutInterruptions(500);
		}
	}
	
}
