package sneer.pulp.bandwidth.tests;

import static sneer.brickness.environments.Environments.my;

import org.junit.Test;

import sneer.pulp.bandwidth.BandwidthCounter;
import sneer.pulp.clock.Clock;
import sneer.pulp.config.persistence.testsupport.BrickTest;
import wheel.testutil.SignalUtils;

public class BandwidthConsolidationTest extends BrickTest {
	
	@Test
	public void test() throws Exception {
		Clock _clock = my(Clock.class);		
		BandwidthCounter _subject = my(BandwidthCounter.class);		
		
		SignalUtils.waitForValue(0, _subject.downloadSpeed());
		SignalUtils.waitForValue(0, _subject.uploadSpeed());
		
		_subject.received(1024*4);
		_subject.sent(1024*40);
		SignalUtils.waitForValue(0, _subject.downloadSpeed());
		SignalUtils.waitForValue(0, _subject.uploadSpeed());
		
		_clock.advanceTime(4000);
		SignalUtils.waitForValue(1, _subject.downloadSpeed());
		SignalUtils.waitForValue(10, _subject.uploadSpeed());
		
		_subject.received(1024*50);
		_subject.sent(1024*5);
		SignalUtils.waitForValue(1, _subject.downloadSpeed());
		SignalUtils.waitForValue(10, _subject.uploadSpeed());
		
		_clock.advanceTime(5000);
		SignalUtils.waitForValue(10, _subject.downloadSpeed());
		SignalUtils.waitForValue(1, _subject.uploadSpeed());
	}
}