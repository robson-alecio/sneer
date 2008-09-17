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

	@Inject @SuppressWarnings("unused") //Interesting, isnt it? ;)
	static private ClockTicker _subject;

	@Test (timeout = 3000)
	public void testTicking() {
		waitForATick();
		waitForATick();
	}

	private void waitForATick() {
		long t0 = _clock.time();
		while (t0 == _clock.time());
			Threads.sleepWithoutInterruptions(1);
	}
	
}
