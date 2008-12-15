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
		
		_subject.packetReceived(10);
		_subject.packetSended(10);
		SignalUtils.waitForValue(0, _subject.downloadSpeed());
		SignalUtils.waitForValue(0, _subject.uploadSpeed());
		
		_subject.packetReceived(10);
		_subject.packetSended(10);
		SignalUtils.waitForValue(0, _subject.downloadSpeed());
		SignalUtils.waitForValue(0, _subject.uploadSpeed());
		
		_clock.advanceTime(1000);
		SignalUtils.waitForValue(20, _subject.downloadSpeed());
		SignalUtils.waitForValue(20, _subject.uploadSpeed());
		
		_subject.packetReceived(10);
		_subject.packetSended(10);
		SignalUtils.waitForValue(20, _subject.downloadSpeed());
		SignalUtils.waitForValue(20, _subject.uploadSpeed());
		
		_clock.advanceTime(1000);
		SignalUtils.waitForValue(10, _subject.downloadSpeed());
		SignalUtils.waitForValue(10, _subject.uploadSpeed());
	}
}