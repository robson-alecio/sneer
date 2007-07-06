package wheel.reactive.tests;

import junit.framework.TestCase;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public abstract class ConnectionTest extends TestCase {

	protected abstract Signal<Object> output();
	protected abstract Omnivore<Object> input();

	private Object _outputValue1;
	private Object _outputValue2;
	private Object _outputValue3;

	public void testConnection() {
		testConnectionWith("banana");
		testConnectionWith(new Integer(42));
	}
	
	private void testConnectionWith(Object object) {
		input().consume(object);
		assertEquals(object, _outputValue1);
		assertEquals(object, _outputValue2);
		assertEquals(object, _outputValue3);
	}

	@Override
	protected void setUp() throws Exception {
		output().addReceiver(new Omnivore<Object>(){
			public void consume(Object newValue) {
				_outputValue1 = newValue;
			}
		});
		output().addTransientReceiver(new Omnivore<Object>(){
			public void consume(Object newValue) {
				_outputValue2 = newValue;
			}
		});
		output().addReceiver(new Omnivore<Object>(){
			public void consume(Object newValue) {
				_outputValue3 = newValue;
			}
		});
	}

}
