package sneer.pulp.reactive.impl;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.events.EventSource;
import wheel.reactive.impl.EventReceiver;

class ReceiversImpl {
	
	private static final Set<Object> _referencesToAvoidGC = new HashSet<Object>();
	
 	static <T> void receive(Object owner, final Consumer<? super T> delegate, final EventSource<? extends T>... sources) {
		final WeakReference<Object> weakOwner = new WeakReference<Object>(owner);

		new EventReceiver<T>(sources) {
			{ _referencesToAvoidGC.add(this); }

			@Override
			public void consume(T value) {
				if (weakOwner.get() == null) {
					_referencesToAvoidGC.remove(this);
					return;
				}
				delegate.consume(value);
			}
		};
 	}
	
//	static <T> void receive(Object owner, final Consumer<? super T> delegate, final EventSource<? extends T>... sources) {
//		new EventReceiver<T>(owner, delegate, sources); 
//	}
//
//	/** Instances of this class hold a reference to the EventSources (Signals) they are receiving, so that these sources are not GCd. */
//	private static class EventReceiver<T> implements Consumer<T> {
//
//		private static final WeakReferenceKeeper _weakReferenceKeeper = my(WeakReferenceKeeper.class);
//
//		@SuppressWarnings("unused")	private final Object _referenceToAvoidGC;
//		private final Consumer<? super T> _delegate;
//
//		public EventReceiver(Object owner, final Consumer<? super T> delegate, EventSource<? extends T>... eventSources) {
//			_delegate = delegate;
//			_weakReferenceKeeper.keep(owner, this);
//			_referenceToAvoidGC = eventSources;
//
//			for (EventSource<? extends T> source : eventSources)
//				source.addReceiver(this);
//		}
//
//		@Override
//		public void consume(T value) { 
//			_delegate.consume(value);
//		}
//	}
}
