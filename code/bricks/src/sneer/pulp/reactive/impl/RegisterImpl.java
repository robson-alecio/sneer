package sneer.pulp.reactive.impl;

import java.lang.ref.WeakReference;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;

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
			
			if (_output == null) return;
			((AbstractSignal<T>)output()).notifyReceivers(newValue);
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
		if (_output == null)
			_output = new WeakReference<AbstractSignal<T>>(new MyOutput());
		else if	(_output.get() == null) {
			_output = new WeakReference<AbstractSignal<T>>(new MyOutput());
		}
		
		return _output.get();
	}


	public Consumer<T> setter() {
		return new MySetter();
	}

}