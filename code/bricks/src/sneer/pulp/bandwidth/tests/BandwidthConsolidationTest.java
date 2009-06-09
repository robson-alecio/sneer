package sneer.pulp.bandwidth.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.bandwidth.BandwidthCounter;
import sneer.pulp.clock.Clock;
import sneer.pulp.reactive.SignalUtils;

public class BandwidthConsolidationTest extends BrickTest {
	
	@Test
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