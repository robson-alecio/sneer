package sneer.bricks.pulp.reactive.impl;

import java.lang.ref.WeakReference;

import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.lang.Consumer;

class RegisterImpl<T> implements Register<T> {

	class MyOutput extends AbstractSignal<T> {

		@Override
		public T currentValue() {
			return _currentValue;
		}
	}

	class MySetter implements Consumer<T> {

		@Override
		public void consume(T newValue) {
			if (isSameValue(newValue)) return;

			_currentValue = newValue;

			if (!isOutputInitialized()) return;
			_output.get().notifyReceivers(newValue);
		}
	}

	private T _currentValue;
	private WeakReference<AbstractSignal<T>> _output;

	public RegisterImpl(T initialValue) {
		setter().consume(initialValue);
	}

	private boolean isSameValue(T value) {
		if (value == _currentValue) return true; 
		if (value != null && value.equals(_currentValue)) return true;

		return false;
	}

	public synchronized Signal<T> output() {
		if (!isOutputInitialized())
			_output = new WeakReference<AbstractSignal<T>>(new MyOutput());

		return _output.get();
	}

	private boolean isOutputInitialized() {
		return _output != null && _output.get() != null;
	}

	public Consumer<T> setter() {
		return new MySetter();
	}
}