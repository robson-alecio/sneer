//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package sneer.pulp.reactive.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventNotifiers;
import sneer.pulp.reactive.Signal;
import sneer.software.lang.Consumer;


abstract class AbstractSignal<T> implements Signal<T> {

	EventNotifier<T> _notifier = my(EventNotifiers.class).create(new Consumer<Consumer<? super T>>(){@Override public void consume(Consumer<? super T> newReceiver) {
		newReceiver.consume(currentValue());
	}});
	
	@Override
	public String toString() {
		T currentValue = currentValue();
		if (currentValue == null) return "null";
		return currentValue.toString();
	}

	protected void notifyReceivers(T value) {
		_notifier.notifyReceivers(value);
	}

	public void removeReceiver(Object receiver) {
		_notifier.output().removeReceiver(receiver);
	}

	public void addReceiver(Consumer<? super T> receiver) {
		_notifier.output().addReceiver(receiver);
	}
	
}