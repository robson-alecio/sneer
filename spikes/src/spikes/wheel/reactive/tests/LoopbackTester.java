package spikes.wheel.reactive.tests;

import static sneer.foundation.commons.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.events.receivers.impl.Solder;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;

public class LoopbackTester {

	public LoopbackTester(Signal<Object> input, Consumer<Object> output) {
		_output = output;
				
		Signal<Object> castedInput = input;

		_referenceToAvoidGc1 = new Solder<Object>(castedInput, _inputValue1.setter());
		_referenceToAvoidGc2 = new Solder<Object>(castedInput, _inputValue2.setter());
		_referenceToAvoidGc3 = new Solder<Object>(castedInput, _inputValue3.setter());
	}
	
	private final Register<Object> _inputValue1 = my(Signals.class).newRegister(null);
	private final Register<Object> _inputValue2 = my(Signals.class).newRegister(null);
	private final Register<Object> _inputValue3 = my(Signals.class).newRegister(null);
	
	private final Consumer<Object> _output;
	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc1;
	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc2;
	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc3;

	public void test() {
		testWithStrings();
		testWithIntegers();
	}
	
	public void testWithStrings() {
		testLoopbackWith("foo");
		testLoopbackWith("bar");
	}

	public void testWithIntegers() {
		testLoopbackWith(42);
		testLoopbackWith(17);
	}
	
	private void testLoopbackWith(Object object) {
		_output.consume(object);
		
		waitAndAssertEquals(object, _inputValue1.output());
		waitAndAssertEquals(object, _inputValue2.output());
		waitAndAssertEquals(object, _inputValue3.output());
	}

	private void waitAndAssertEquals(Object expected, Signal<Object> input) {
		long t0 = System.currentTimeMillis();
		while (!expected.equals(input.currentValue())) {
			Thread.yield();
			if (System.currentTimeMillis() - t0 > 1000)
				throw new RuntimeException("Timeout");
		}
	}

}
