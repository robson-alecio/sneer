package sneer.pulp.bandwidth.tests;

import static wheel.lang.Environments.my;

import org.junit.Test;

import sneer.pulp.bandwidth.BandwidthCounter;
import sneer.pulp.clock.Clock;
import tests.TestInContainerEnvironment;
import wheel.testutil.SignalUtils;

public class BandwidthCounterTest extends TestInContainerEnvironment {
	
	@Test
	public void testConsolidation() throws Exception {
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