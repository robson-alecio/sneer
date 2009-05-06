package wheel.reactive.impl;

import java.util.Collection;
import java.util.Iterator;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.collections.SetChange;
import sneer.pulp.reactive.collections.SetSignal;


public class SetSignalOwnerReference<T> extends AbstractOwnerReference<T> implements SetSignal<T> {
	
	private final SetSignal<T> _delegate;
	
	public SetSignalOwnerReference(SetSignal<T> delegate, Object objectToSaveFromGC) {
		super(objectToSaveFromGC);
		_delegate = delegate;
	}

	@Override public void addReceiver(Consumer<? super SetChange<T>> receiver) { _delegate.addReceiver(receiver); }
	@Override public void removeReceiver(Object receiver) { _delegate.removeReceiver(receiver); }
	@Override public Collection<T> currentElements() { return _delegate.currentElements(); }
	@Override public int currentSize() { return _delegate.currentSize(); }
	@Override public Signal<Integer> size() { return _delegate.size(); }
	@Override public Iterator<T> iterator() { return _delegate.iterator(); }
	@Override public boolean currentContains(T element) { return _delegate.currentContains(element); }

}