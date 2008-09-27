package wheel.reactive.impl.tests;

import junit.framework.TestCase;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.tests.LoopbackTester;

public class RegisterImplTest extends TestCase {

	public void testNotification() {
		RegisterImpl<Object> subject = new RegisterImpl<Object>(null);
		new LoopbackTester(subject.output(), subject.setter()).test();
	}
	

}
