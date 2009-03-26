package sneer.pulp.reactive.gates.logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static sneer.commons.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.brickness.testsupport.BrickTestRunner;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.gates.logic.LogicGates;
import sneer.pulp.reactive.impl.RegisterImpl;
import wheel.lang.Consumer;
import wheel.reactive.impl.EventReceiver;

@RunWith(BrickTestRunner.class)
public class AndTest {

	Register<Boolean> _input1 = new RegisterImpl<Boolean>(false);
	Register<Boolean> _input2 = new RegisterImpl<Boolean>(false);
	Signal<Boolean> _andResult = my(LogicGates.class).and(_input1.output(), _input2.output());
	List<Boolean> _recorded = new ArrayList<Boolean>();
	
	Object _referenceToAvoidGc = new EventReceiver<Boolean>(_andResult){ @Override public void consume(Boolean value) {
		_recorded.add(value);
	}};

	@Test
	public void test() {
		
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
