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

		signalsUtils.waitForValue(subject.downloadSpeed(), 0);
		signalsUtils.waitForValue(subject.uploadSpeed(), 0);

		subject.received(1024*4);
		subject.sent(1024*40);
		signalsUtils.waitForValue(subject.downloadSpeed(), 0);
		signalsUtils.waitForValue(subject.uploadSpeed(), 0);

		clock.advanceTime(4000);
		signalsUtils.waitForValue(subject.downloadSpeed(), 1);
		signalsUtils.waitForValue(subject.uploadSpeed(), 10);

		subject.received(1024*50);
		subject.sent(1024*5);
		signalsUtils.waitForValue(subject.downloadSpeed(), 1);
		signalsUtils.waitForValue(subject.uploadSpeed(), 10);

		clock.advanceTime(5000);
		signalsUtils.waitForValue(subject.downloadSpeed(), 10);
		signalsUtils.waitForValue(subject.uploadSpeed(), 1);
	}
}