package wheel.reactive.impl.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signals;
import wheel.reactive.tests.LoopbackTester;

public class RegisterImplTest extends BrickTest {

	@Test
	public void testNotification() {
		Register<Object> subject = my(Signals.class).newRegister(null);
		new LoopbackTester(subject.output(), subject.setter()).test();
	}
	

}
