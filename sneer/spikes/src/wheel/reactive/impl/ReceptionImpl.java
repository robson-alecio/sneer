package wheel.reactive.impl;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.events.EventSource;
import sneer.pulp.reactive.Reception;

/** Instances of this class hold a reference to the EventSources (Signals) they are receiving, so that these sources are not GCd. */
public abstract class ReceptionImpl<T> implements Consumer<T>, Reception {
	
	private final EventSource<? extends T>[] _sourcesReferenceToAvoidGc;

	public ReceptionImpl(EventSource<? extends T>... eventSources) {
		for (EventSource<? extends T> source : eventSources)
			source.addReceiver(this);
		
		_sourcesReferenceToAvoidGc = eventSources;
	}

	@Override
	public void dispose() {
		for (EventSource<? extends T> source : _sourcesReferenceToAvoidGc)
			source.removeReceiver(this);
	}

	
	
}
