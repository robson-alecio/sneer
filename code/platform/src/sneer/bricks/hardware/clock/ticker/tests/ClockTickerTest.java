package sneer.bricks.hardware.clock.ticker.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.clock.ticker.ClockTicker;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.Consumer;


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
		final Latch latch = my(Threads.class).newLatch();
		my(Signals.class).receive(_clock.time(), new Consumer<Long>() { @Override public void consume(Long value) {
			latch.open();
		}});
		latch.waitTillOpen();
	}
	
}
