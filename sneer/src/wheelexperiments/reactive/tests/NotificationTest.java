package wheelexperiments.reactive.tests;

import wheelexperiments.reactive.AbstractSignal;
import wheelexperiments.reactive.Receiver;
import wheelexperiments.reactive.Signal;

public class NotificationTest extends ConnectionTest {

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
				_notifier.notifyReceivers(newValue);
			}
		};
	}

	private static class MyNotifier extends AbstractSignal<Object> {

		private Object _value;

		public Object currentValue() {
			return _value;
		}

		@Override
		public void notifyReceivers(Object newValue) {
			super.notifyReceivers(newValue);
		}
	}
	
}
