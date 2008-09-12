package sneer.pulp.clock.realtime.impl.tests;

import org.junit.Ignore;
import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.clock.realtime.RealtimeClock;

public class RealtimeClockTest extends TestThatIsInjected {
	
	@Inject
	static private RealtimeClock _subject;
	
	@Test (timeout = 1000)
	public void testCurrentTime() {
	   while (_subject.currentTimeMillis() != System.currentTimeMillis());
	}

	@Ignore
	@Test (expected = IllegalStateException.class)
	public void testSetOwnerMoreThatOnce() {
		_subject.setOwner(new Runnable() { @Override public void run() {}});
		_subject.setOwner(new Runnable() { @Override public void run() {}});
	}
	
	@Ignore
	@Test (timeout = 1000)
	public void testSleep() {
		_subject.setOwner(new Runnable() { @Override public void run() {
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
		}});
		
		while (_subject.currentTimeMillis() != System.currentTimeMillis());
	}
	
}