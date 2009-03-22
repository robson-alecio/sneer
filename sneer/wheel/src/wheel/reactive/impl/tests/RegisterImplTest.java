package wheel.reactive.impl.tests;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.reactive.impl.RegisterImpl;
import wheel.reactive.tests.LoopbackTester;

public class RegisterImplTest extends BrickTest {

	@Test
	public void testNotification() {
		RegisterImpl<Object> subject = new RegisterImpl<Object>(null);
		new LoopbackTester(subject.output(), subject.setter()).test();
	}
	

}
