package spikes.wheel.reactive.impl.tests;

import static sneer.foundation.commons.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.brickness.testsupport.BrickTest;
import spikes.wheel.reactive.tests.LoopbackTester;

public class RegisterImplTest extends BrickTest {

	@Test
	public void testNotification() {
		Register<Object> subject = my(Signals.class).newRegister(null);
		new LoopbackTester(subject.output(), subject.setter()).test();
	}
	

}
