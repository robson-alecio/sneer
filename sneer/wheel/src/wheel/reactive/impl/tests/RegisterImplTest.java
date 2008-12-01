package wheel.reactive.impl.tests;

import org.junit.Test;
import org.junit.runner.RunWith;

import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.tests.LoopbackTester;
import wheel.testutil.WheelEnvironment;

@RunWith(WheelEnvironment.class)
public class RegisterImplTest {

	@Test
	public void testNotification() {
		RegisterImpl<Object> subject = new RegisterImpl<Object>(null);
		new LoopbackTester(subject.output(), subject.setter()).test();
	}
	

}
