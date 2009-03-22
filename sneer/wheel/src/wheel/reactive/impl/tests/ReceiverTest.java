package wheel.reactive.impl.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.brickness.testsupport.BrickTestRunner;
import sneer.pulp.reactive.impl.RegisterImpl;
import wheel.reactive.impl.EventReceiver;

@RunWith(BrickTestRunner.class)
public class ReceiverTest {
	
	@Test
	public void testAddToSignal() {
		final StringBuilder received = new StringBuilder();
		RegisterImpl<String> register1 = new RegisterImpl<String>(null);
		RegisterImpl<String> register2 = new RegisterImpl<String>("hey");
		@SuppressWarnings("unused")
		Object referenceToAvoidGc = new EventReceiver<String>(register1.output(), register2.output()) {@Override public void consume(String value) {
			received.append(value);
		}};
		
		assertEquals("nullhey", received.toString());

		register1.setter().consume("foo");
		register2.setter().consume("bar");
		
		assertEquals("nullheyfoobar", received.toString());

		register1.setter().consume("baz1");
		register2.setter().consume("baz2");
		assertEquals("nullheyfoobarbaz1baz2", received.toString());
	}

}
