package sneer.pulp.clock.tests;

import org.junit.Test;

import sneer.pulp.clock.Clock;
import sneer.pulp.exceptionhandling.tests.ShieldingTestBase;
import sneer.pulp.threadpool.Stepper;
import static wheel.lang.Environments.my;

public class ClockShieldingTest extends ShieldingTestBase {

	private Clock _subject = my(Clock.class);
	
	private final Stepper _stepper = new Stepper() { @Override public boolean step() {
		return false;
	}};
	
	@Test
	public void testAlarmShielding() {
		_subject.wakeUpEvery(10, _stepper);
		_subject.advanceTime(10);
	}
}
