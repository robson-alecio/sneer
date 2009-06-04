package spikes.wheel.reactive.impl.tests;

import static org.junit.Assert.assertEquals;
import static sneer.commons.environments.Environments.my;

import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.brickness.testsupport.BrickTestRunner;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signals;

@RunWith(BrickTestRunner.class)
public class ReceiverTest {
	
	@Test
	public void testAddToSignal() {
		final StringBuilder received = new StringBuilder();
		Register<String> register1 = my(Signals.class).newRegister(null);
		Register<String> register2 = my(Signals.class).newRegister("hey");

		@SuppressWarnings("unused") final Object referenceToAvoidGc = my(Signals.class).receive(new Consumer<String>() {@Override public void consume(String value) {
			received.append(value);
		}}, register1.output(), register2.output());

		assertEquals("nullhey", received.toString());

		register1.setter().consume("foo");
		register2.setter().consume("bar");

		assertEquals("nullheyfoobar", received.toString());

		register1.setter().consume("baz1");
		register2.setter().consume("baz2");
		assertEquals("nullheyfoobarbaz1baz2", received.toString());
	}
}
