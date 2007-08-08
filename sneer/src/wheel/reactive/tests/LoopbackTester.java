package wheel.reactive.tests;

import static junit.framework.Assert.assertEquals;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class LoopbackTester {

	public LoopbackTester(Signal<?> input, Omnivore<?> output) {
		_output = Casts.uncheckedGenericCast(output);
				
		Signal<Object> castedInput = Casts.uncheckedGenericCast(input);
		castedInput.addReceiver(new Omnivore<Object>() { public void consume(Object value) {
			_inputValue1 = value;
		}});
		castedInput.addReceiver(new Omnivore<Object>() { public void consume(Object value) {
			_inputValue2 = value;
		}});
		castedInput.addTransientReceiver(new Omnivore<Object>() { public void consume(Object value) {
			_inputValue3 = value;
		}});
	}
	
	private final Omnivore<Object> _output;

	private Object _inputValue1;
	private Object _inputValue2;
	private Object _inputValue3;

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
		Threads.sleepWithoutInterruptions(500); //FixUrgent Tests cannot wait!!! Detect signal change.
		assertEquals(object, _inputValue1);
		assertEquals(object, _inputValue2);
		assertEquals(object, _inputValue3);
	}

}
