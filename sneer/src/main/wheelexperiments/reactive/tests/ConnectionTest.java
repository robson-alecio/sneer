package wheelexperiments.reactive.tests;

import junit.framework.TestCase;
import wheelexperiments.reactive.Receiver;
import wheelexperiments.reactive.Signal;

public abstract class ConnectionTest extends TestCase {

	private Object _outputValue;

	@Override
	protected void setUp() throws Exception {
		output().addReceiver(new Receiver<Object>(){
			public void receive(Object newValue) {
				_outputValue = newValue;
			}
		});
	}

	protected abstract Signal<Object> output();

	public void testConnection() {
		input().receive("banana");
		assertEquals("banana", _outputValue);
		
		input().receive(new Integer(42));
		assertEquals(new Integer(42), _outputValue);
	}

	protected abstract Receiver<Object> input();


	
}
