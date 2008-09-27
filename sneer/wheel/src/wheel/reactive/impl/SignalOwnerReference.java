package wheel.reactive.impl;

import java.util.Collection;
import java.util.Iterator;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListValueChange;

/** Avoid garbage collection of another object while the wrapped signal has references. Useful for implementing gates. */
public class SignalOwnerReference<T> implements Signal<T> {
	
	private final Signal<T> _delegate;
	@SuppressWarnings("unused") //Avoid GarbageCollection of the owner.
	private final Object _referenceToAvoidGC;

	public SignalOwnerReference(Signal<T> delegate, Object objectToSaveFromGC) {
		_delegate = delegate;
		_referenceToAvoidGC = objectToSaveFromGC;
	}

	@Override
	public void addListReceiver(Omnivore<ListValueChange> receiver) {
		_delegate.addListReceiver(receiver);
	}

	public void addReceiver(Omnivore<? super T> receiver) {
		_delegate.addReceiver(receiver);
	}

	@Override
	public void addSetReceiver(Omnivore<SetValueChange<T>> receiver) {
		_delegate.addSetReceiver(receiver);
	}

	public Collection<T> currentElements() {
		return _delegate.currentElements();
	}

	public T currentGet(int index) {
		return _delegate.currentGet(index);
	}

	public int currentSize() {
		return _delegate.currentSize();
	}

	public T currentValue() {
		return _delegate.currentValue();
	}

	public Iterator<T> iterator() {
		return _delegate.iterator();
	}

	public void removeListReceiver(Object receiver) {
		_delegate.removeListReceiver(receiver);
	}

	public void removeReceiver(Object receiver) {
		_delegate.removeReceiver(receiver);
	}

	public void removeSetReceiver(Object receiver) {
		_delegate.removeSetReceiver(receiver);
	}

	public Signal<Integer> size() {
		return _delegate.size();
	}

}
