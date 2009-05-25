package sneer.pulp.reactive.impl;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.events.EventSource;
import sneer.pulp.reactive.Reception;

class ReceiversImpl {
	
	private static final Set<Object> _referencesToAvoidGC = new HashSet<Object>();
	
 	static <T> void receive(Object owner, final Consumer<? super T> delegate, final EventSource<? extends T>... sources) {
		final WeakReference<Object> weakOwner = new WeakReference<Object>(owner);

		new ReceptionImpl<T>(sources) {
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

 	static <T> Reception receive(final Consumer<? super T> receiver, final EventSource<? extends T>... sources) {
		return new ReceptionImpl<T>(sources) { @Override public void consume(T value) {
			receiver.consume(value);
		}};
 	}

}
