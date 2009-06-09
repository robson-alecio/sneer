package sneer.bricks.pulp.reactive.impl;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.reactive.Reception;

class ReceiversImpl {

 	/** Instances of this class hold a reference to the EventSources (Signals) they are receiving, so that these sources are not GCd. */
 	private static abstract class ReceptionImpl<T> implements Consumer<T>, Reception {

 		private final EventSource<? extends T>[] _sourcesReferenceToAvoidGc;

 		private ReceptionImpl(EventSource<? extends T>... eventSources) {
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
 
 	static <T> Reception receive(final Consumer<? super T> receiver, final EventSource<? extends T>... sources) {
		return new ReceptionImpl<T>(sources) { @Override public void consume(T value) {
			receiver.consume(value);
		}};
 	}
}
