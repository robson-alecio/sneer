package wheel.reactive.impl;

import sneer.pulp.events.EventSource;
import sneer.software.lang.Consumer;

/** Instances of this class hold a reference to the EventSources (Signals) they are receiving, so that these sources are not GCd. */
public abstract class EventReceiver<T> implements Consumer<T> {
	
	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc;

	public EventReceiver(EventSource<? extends T>... eventSources) {
		for (EventSource<? extends T> source : eventSources)
			source.addReceiver(this);
		
		_referenceToAvoidGc = eventSources;
	}

}
