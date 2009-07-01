package sneer.bricks.pulp.bandwidth.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.pulp.bandwidth.BandwidthCounter;
import sneer.bricks.pulp.reactive.SignalUtils;
import sneer.foundation.brickness.testsupport.BrickTest;

public class BandwidthConsolidationTest extends BrickTest {
	
	@Test (timeout = 3000)
	public void test() throws Exception {
		final SignalUtils signalsUtils = my(SignalUtils.class);
		final Clock clock = my(Clock.class);		
		final BandwidthCounter subject = my(BandwidthCounter.class);

		signalsUtils.waitForValue(0, subject.downloadSpeed());
		signalsUtils.waitForValue(0, subject.uploadSpeed());

		subject.received(1024*4);
		subject.sent(1024*40);
		signalsUtils.waitForValue(0, subject.downloadSpeed());
		signalsUtils.waitForValue(0, subject.uploadSpeed());

		clock.advanceTime(4000);
		signalsUtils.waitForValue(1, subject.downloadSpeed());
		signalsUtils.waitForValue(10, subject.uploadSpeed());

		subject.received(1024*50);
		subject.sent(1024*5);
		signalsUtils.waitForValue(1, subject.downloadSpeed());
		signalsUtils.waitForValue(10, subject.uploadSpeed());

		clock.advanceTime(5000);
		signalsUtils.waitForValue(10, subject.downloadSpeed());
		signalsUtils.waitForValue(1, subject.uploadSpeed());
	}
}