package wheel.reactive.impl;

import java.util.Iterator;
import java.util.List;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.collections.ListChange;
import sneer.pulp.reactive.collections.ListSignal;


public class ListSignalOwnerReference<T> extends AbstractOwnerReference<T> implements ListSignal<T> {
	
	private final ListSignal<T> _delegate;
	
	public ListSignalOwnerReference(ListSignal<T> delegate, Object objectToSaveFromGC) {
		super(objectToSaveFromGC);
		_delegate = delegate;
	}

	@Override public void addReceiver(Consumer<? super ListChange<T>> receiver) { _delegate.addReceiver(receiver); }
	@Override public T currentGet(int index) { return _delegate.currentGet(index); }
	@Override public void removeReceiver(Object receiver) { _delegate.removeReceiver(receiver); }
	@Override public List<T> currentElements() { return _delegate.currentElements(); }
	@Override public int currentIndexOf(T element) { return _delegate.currentIndexOf(element); }
	@Override public int currentSize() { return _delegate.currentSize(); }
	@Override public Signal<Integer> size() { return _delegate.size(); }
	@Override public Iterator<T> iterator() { return _delegate.iterator(); }
}