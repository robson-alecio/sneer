package sneer.bricks.pulp.reactive.gates.logic.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.gates.logic.LogicGates;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.Consumer;

public class AndTest extends BrickTest {

	private final Signals SIGNALS = my(Signals.class);

	private final Register<Boolean> _input1 = SIGNALS.newRegister(false);
	private final Register<Boolean> _input2 = SIGNALS.newRegister(false);
	private final Signal<Boolean> _andResult = my(LogicGates.class).and(_input1.output(), _input2.output());
	private final List<Boolean> _recorded = new ArrayList<Boolean>();

	@Test
	public void test() {
		@SuppressWarnings("unused") final Object _referenceToAvoidGc = SIGNALS.receive(_andResult, new Consumer<Boolean>(){ @Override public void consume(Boolean value) {
			_recorded.add(value);
		}});

		assertFalse(_andResult.currentValue());

		_input1.setter().consume(true);
		assertFalse(_andResult.currentValue());

		_input2.setter().consume(true);
		assertTrue(_andResult.currentValue());

		_input2.setter().consume(false);
		assertFalse(_andResult.currentValue());

		expectOutputSequence(false, true, false);
	}
	
	private void expectOutputSequence(boolean... expected) {
		assertEquals(expected.length, _recorded.size());

		for (int i = 0; i < expected.length; i++)
			assertEquals(expected[i], _recorded.get(i)); 
	}
}
