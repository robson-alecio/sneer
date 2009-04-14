package sneer.pulp.events.impl;

import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventNotifiers;
import sneer.software.lang.Consumer;

class EventNotifiersImpl implements EventNotifiers {

	@Override
	public <T> EventNotifier<T> create() {
		return create(null);
	}

	@Override
	public <T> EventNotifier<T> create(Consumer<Consumer<? super T>> receiverHandler) {
		return new EventNotifierImpl<T>(receiverHandler);
	}

}