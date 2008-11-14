package wheel.reactive.impl;

import java.util.Iterator;

import wheel.lang.Consumer;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;

public class ListSignalOwnerReference<T> extends AbstractOwnerReference<T> implements ListSignal<T> {
	
	private final ListSignal<T> _delegate;
	
	public ListSignalOwnerReference(ListSignal<T> delegate, Object objectToSaveFromGC) {
		super(objectToSaveFromGC);
		_delegate = delegate;
	}

	@Override public void addListReceiver(Consumer<ListValueChange<T>> receiver) { _delegate.addListReceiver(receiver); }
	@Override public T currentGet(int index) { return _delegate.currentGet(index); }
	@Override public void removeListReceiver(Object receiver) { _delegate.removeListReceiver(receiver); }
	@Override public T[] toArray() { return _delegate.toArray(); }
	@Override public int currentSize() { return _delegate.currentSize(); }
	@Override public Signal<Integer> size() { return _delegate.size(); }
	@Override public Iterator<T> iterator() { return _delegate.iterator(); }
}