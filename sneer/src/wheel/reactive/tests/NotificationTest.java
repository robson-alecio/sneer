package wheel.reactive.tests;

import wheel.reactive.AbstractSignal;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;

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
