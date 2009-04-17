package sneer.pulp.reactive.impl;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import sneer.pulp.events.EventSource;
import sneer.software.lang.Consumer;
import wheel.reactive.impl.EventReceiver;

class ReceiversImpl {

	private static final Set<Object> _referencesToAvoidGC = new HashSet<Object>();

	static <T> void receive(Object owner, final Consumer<? super T> delegate, EventSource<? extends T>... source) {
		final WeakReference<Object> weakOwner = new WeakReference<Object>(owner);

		new EventReceiver<T>(source) {
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
}
