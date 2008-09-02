package wheel.reactive.impl.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import wheel.reactive.impl.Receiver;
import wheel.reactive.impl.RegisterImpl;

public class ReceiverTest {
	
	final StringBuilder received = new StringBuilder();
	final RegisterImpl<String> register = new RegisterImpl<String>(null);
	final Receiver<String> receiver = new Receiver<String>(register.output()) {@Override public void consume(String value) {
		received.append(value);
	}};
	
	@Test
	public void testAddToSignal() {
		
		consume("foo");
		
		final RegisterImpl<String> register2 = new RegisterImpl<String>(null);
		register2.setter().consume("bar");
		
		assertEquals("foo", received.toString());
		receiver.addToSignal(register2.output());
		assertEquals("foobar", received.toString());

		consume("baz1");
		register2.setter().consume("baz2");
		assertEquals("foobarbaz1baz2", received.toString());
	}
	
	@Test
	public void testRemoveFromSignals() {
		
		consume("foo");
		consume("bar");
		assertEquals("foobar", received.toString());
		
		receiver.removeFromSignals();
		
		consume("baz");
		assertEquals("foobar", received.toString());
		
	}

	private void consume(final String value) {
		register.setter().consume(value);
	}

}
