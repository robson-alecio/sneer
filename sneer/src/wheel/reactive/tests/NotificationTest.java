package wheel.reactive.tests;

import wheel.lang.Omnivore;
import wheel.reactive.AbstractSignal;
import wheel.reactive.Signal;

public class NotificationTest extends ConnectionTest {

	private MyNotifier _notifier = new MyNotifier();

	@Override
	protected Signal<Object> output() {
		return _notifier;
	}

	@Override
	protected Omnivore<Object> input() {
		return new Omnivore<Object>() {
			public void consume(Object newValue) {
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
