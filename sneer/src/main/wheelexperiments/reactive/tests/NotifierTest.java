package wheelexperiments.reactive.tests;

import wheelexperiments.reactive.Notifier;
import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.Signal.Receiver;

public class NotifierTest extends ConnectionTest {

	private MyNotifier _notifier = new MyNotifier();

	@Override
	protected Signal<Object> output() {
		return _notifier;
	}

	@Override
	protected Receiver<Object> input() {
		return new Receiver<Object>() {
			public void receive(Object newValue) {
				_notifier._value = newValue;
				_notifier.notifyReceivers();
			}
		};
	}

	private static class MyNotifier extends Notifier<Object> {

		private Object _value;

		public Object currentValue() {
			return _value;
		}

	}
	
}
