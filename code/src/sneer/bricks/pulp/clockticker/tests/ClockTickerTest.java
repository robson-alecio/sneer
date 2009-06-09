package sneer.bricks.pulp.clockticker.tests;

import static sneer.foundation.commons.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.pulp.clock.Clock;
import sneer.bricks.pulp.clockticker.ClockTicker;
import sneer.bricks.pulp.threads.Threads;
import sneer.foundation.brickness.testsupport.BrickTest;

public class ClockTickerTest extends BrickTest {

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
			my(Threads.class).sleepWithoutInterruptions(1);
	}
	
}
