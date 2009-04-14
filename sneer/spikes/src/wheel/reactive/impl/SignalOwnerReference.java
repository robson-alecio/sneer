package wheel.reactive.impl;

import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public class SignalOwnerReference<T> extends AbstractOwnerReference<T> implements Signal<T> {
	
	private final Signal<T> _delegate;
	
	public SignalOwnerReference(Signal<T> delegate, Object objectToSaveFromGC) {
		super(objectToSaveFromGC);
		_delegate = delegate;
	}

	@Override public void addReceiver(Consumer<? super T> receiver) { _delegate.addReceiver(receiver); }
	@Override public T currentValue() { return _delegate.currentValue(); }
	@Override public void removeReceiver(Object receiver) { _delegate.removeReceiver(receiver); }
}