package sneer.bricks.pulp.reactive.impl;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.pulp.events.EventSource;
import sneer.foundation.lang.Consumer;

/** Instances of this class hold a reference to the EventSources (Signals) they are receiving, so that these sources are not GCd. */
class ReceptionImpl implements Contract {

	
	private final Consumer<?> _receiver;
	private final EventSource<?>[] _sourcesReferenceToAvoidGc;

	
	<T> ReceptionImpl(Consumer<? super T> receiver, EventSource<? extends T>... eventSources) {
		_receiver = receiver;

		for (EventSource<? extends T> source : eventSources){
			if(source==null) continue; //Fix: NPE
			source.addReceiver(receiver);
		}

		_sourcesReferenceToAvoidGc = eventSources;
	}


	@Override
	public void dispose() {
		for (EventSource<?> source : _sourcesReferenceToAvoidGc)
			source.removeReceiver(_receiver);
	}
	
}
