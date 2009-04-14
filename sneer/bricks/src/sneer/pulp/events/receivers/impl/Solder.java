package sneer.pulp.events.receivers.impl;

import sneer.pulp.events.EventSource;
import sneer.software.lang.Consumer;
import wheel.reactive.impl.EventReceiver;

public class Solder<T> {

	private final Consumer<? super T> _delegate;
	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc;

	public Solder(EventSource<? extends T> eventSource, Consumer<? super T> receiver) {
		_delegate = receiver;
		_referenceToAvoidGc = new EventReceiver<T>(eventSource) { @Override public void consume(T event) {
			_delegate.consume(event);
		}};
	}

}
