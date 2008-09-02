package wheel.reactive.impl.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import wheel.reactive.impl.Receiver;
import wheel.reactive.impl.RegisterImpl;

public class ReceiverTest {
	
	@Test
	public void testRemoveFromSignals() {
		
		final StringBuilder received = new StringBuilder();
		final RegisterImpl<String> register = new RegisterImpl<String>(null);
		
		final Receiver<String> receiver = new Receiver<String>(register.output()) {@Override public void consume(String value) {
			received.append(value);
		}};
		
		register.setter().consume("foo");
		register.setter().consume("bar");
		assertEquals("foobar", received.toString());
		
		receiver.removeFromSignals();
		
		register.setter().consume("baz");
		assertEquals("foobar", received.toString());
		
	}

}
