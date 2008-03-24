package wheel.reactive.tests;

import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class LoopbackTester {

	private Register<Object> _inputValue1 = new RegisterImpl<Object>(null);
	private Register<Object> _inputValue2 = new RegisterImpl<Object>(null);
	private Register<Object> _inputValue3 = new RegisterImpl<Object>(null);

	public LoopbackTester(Signal<?> input, Omnivore<?> output) {
		_output = Casts.uncheckedGenericCast(output);
				
		Signal<Object> castedInput = Casts.uncheckedGenericCast(input);
		castedInput.addReceiver(_inputValue1.setter());
		castedInput.addReceiver(_inputValue2.setter());
		castedInput.addTransientReceiver(_inputValue3.setter());
	}
	
	private final Omnivore<Object> _output;


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
