package wheel.reactive.tests;

import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;
import wheel.lang.Types;
import wheel.reactive.Solder;
import wheel.reactive.impl.RegisterImpl;

public class LoopbackTester {

	private Register<Object> _inputValue1 = new RegisterImpl<Object>(null);
	private Register<Object> _inputValue2 = new RegisterImpl<Object>(null);
	private Register<Object> _inputValue3 = new RegisterImpl<Object>(null);

	public LoopbackTester(Signal<?> input, Consumer<?> output) {
		_output = Types.cast(output);
				
		Signal<Object> castedInput = Types.cast(input);

		@SuppressWarnings("unused")
		Object s1 = new Solder<Object>(castedInput, _inputValue1.setter());
		@SuppressWarnings("unused")
		Object s2 = new Solder<Object>(castedInput, _inputValue2.setter());
		@SuppressWarnings("unused")
		Object s3 = new Solder<Object>(castedInput, _inputValue3.setter());
	}
	
	private final Consumer<Object> _output;


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
