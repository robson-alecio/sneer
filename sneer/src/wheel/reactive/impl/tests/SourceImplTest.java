package wheel.reactive.impl.tests;

import junit.framework.TestCase;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.tests.LoopbackTester;

public class SourceImplTest extends TestCase {

	public void testNotification() {
		SourceImpl<Object> subject = new SourceImpl<Object>(null);
		new LoopbackTester(subject.output(), subject.setter()).test();
	}
	

}
