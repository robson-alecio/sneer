package sneer.pulp.reactive.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.pulp.events.EventSource;

class ReceiversImpl {

	static <T> void receive(Object owner, final Consumer<? super T> delegate, final EventSource<? extends T>... sources) {
		new EventReceiver<T>(owner, delegate, sources); 
	}

	/** Instances of this class hold a reference to the EventSources (Signals) they are receiving, so that these sources are not GCd. */
	private static class EventReceiver<T> implements Consumer<T> {

		private static final WeakReferenceKeeper _weakReferenceKeeper = my(WeakReferenceKeeper.class);

		@SuppressWarnings("unused")	private final Object _referenceToAvoidGC;
		private final Consumer<? super T> _delegate;

		public EventReceiver(Object owner, final Consumer<? super T> delegate, EventSource<? extends T>... eventSources) {
			_delegate = delegate;
			_weakReferenceKeeper.keep(owner, this);
			_referenceToAvoidGC = eventSources;

			for (EventSource<? extends T> source : eventSources)
				source.addReceiver(this);
		}

		@Override
		public void consume(T value) { 
			_delegate.consume(value);
		}
	}
}
