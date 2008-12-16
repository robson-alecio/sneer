package sneer.pulp.bandwidth.tests;

import static wheel.lang.Environments.my;

import org.junit.Test;

import sneer.pulp.bandwidth.BandwidthCounter;
import sneer.pulp.clock.Clock;
import tests.TestThatIsInjected;
import wheel.testutil.SignalUtils;

public class BandwidthCounterTest extends TestThatIsInjected {
	
	@Test
	public void testConsolidation() throws Exception {
		Clock _clock = my(Clock.class);		
		BandwidthCounter _subject = my(BandwidthCounter.class);		
		
		_clock.advanceTime(1);
		SignalUtils.waitForValue(0, _subject.downloadSpeed());
		SignalUtils.waitForValue(0, _subject.uploadSpeed());
		
		_subject.received(1024);
		_subject.sent(1024);
		SignalUtils.waitForValue(0, _subject.downloadSpeed());
		SignalUtils.waitForValue(0, _subject.uploadSpeed());
		
		_subject.received(1024);
		_subject.sent(1024);
		SignalUtils.waitForValue(0, _subject.downloadSpeed());
		SignalUtils.waitForValue(0, _subject.uploadSpeed());
		
		_clock.advanceTime(1000);
		SignalUtils.waitForValue(2, _subject.downloadSpeed());
		SignalUtils.waitForValue(2, _subject.uploadSpeed());
		
		_subject.received(1024);
		_subject.sent(1024);
		SignalUtils.waitForValue(2, _subject.downloadSpeed());
		SignalUtils.waitForValue(2, _subject.uploadSpeed());
		
		_clock.advanceTime(1000);
		SignalUtils.waitForValue(1, _subject.downloadSpeed());
		SignalUtils.waitForValue(1, _subject.uploadSpeed());
	}
}