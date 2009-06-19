package sneer.bricks.hardware.clock.ticker.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.clock.ticker.ClockTicker;
import sneer.bricks.hardware.cpu.threads.Threads;
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
