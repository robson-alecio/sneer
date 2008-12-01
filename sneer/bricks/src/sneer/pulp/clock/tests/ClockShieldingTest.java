package sneer.pulp.clock.tests;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.exceptionhandling.tests.ShieldingTestBase;
import sneer.pulp.threadpool.Stepper;

public class ClockShieldingTest extends ShieldingTestBase {

	@Inject static private Clock _subject;
	
	private final Stepper _stepper = new Stepper() { @Override public boolean step() {
		return false;
	}};
	
	@Test
	public void testAlarmShielding() {
		_subject.wakeUpEvery(10, _stepper);
		_subject.advanceTime(10);
	}
}
