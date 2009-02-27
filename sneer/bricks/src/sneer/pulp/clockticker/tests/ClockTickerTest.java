package sneer.pulp.clockticker.tests;

import static sneer.brickness.Environments.my;

import org.junit.Test;

import sneer.pulp.clock.Clock;
import sneer.pulp.clockticker.ClockTicker;
import tests.TestInContainerEnvironment;
import wheel.lang.Threads;

public class ClockTickerTest extends TestInContainerEnvironment {

	private final Clock _clock = my(Clock.class);

	{
		my(ClockTicker.class);
	}

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
