package sneer.bricks.pulp.events.receivers.impl;

import sneer.bricks.pulp.events.EventSource;
import sneer.foundation.lang.Consumer;

public class Solder<T> {

	private final Consumer<? super T> _delegate;

	@SuppressWarnings("unused") private final Object _referenceToAvoidGc;

	public Solder(EventSource<? extends T> eventSource, Consumer<? super T> receiver) {
		_delegate = receiver;

		_referenceToAvoidGc = eventSource.addReceiver(new Consumer<T>() { @Override public void consume(T event) {
			_delegate.consume(event);
		}});
	}
}
