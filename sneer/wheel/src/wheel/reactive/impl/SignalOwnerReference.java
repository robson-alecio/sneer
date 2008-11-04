package wheel.reactive.impl;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

/** Avoid garbage collection of another object while the wrapped signal has references. Useful for implementing gates. */
public class SignalOwnerReference<T> implements Signal<T> {
	
	private final Signal<T> _delegate;
	@SuppressWarnings("unused") //Avoid GarbageCollection of the owner.
	private final Object _referenceToAvoidGC;

	public SignalOwnerReference(Signal<T> delegate, Object objectToSaveFromGC) {
		_delegate = delegate;
		_referenceToAvoidGC = objectToSaveFromGC;
	}

	public void addReceiver(Omnivore<? super T> receiver) {
		_delegate.addReceiver(receiver);
	}

	public T currentValue() {
		return _delegate.currentValue();
	}

	public void removeReceiver(Object receiver) {
		_delegate.removeReceiver(receiver);
	}
}