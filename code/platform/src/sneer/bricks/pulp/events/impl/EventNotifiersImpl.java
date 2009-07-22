package sneer.bricks.pulp.events.impl;

import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.foundation.lang.Consumer;

class EventNotifiersImpl implements EventNotifiers {

	@Override
	public <T> EventNotifier<T> newInstance() {
		return newInstance(null);
	}

	@Override
	public <T> EventNotifier<T> newInstance(Consumer<Consumer<? super T>> receiverHandler) {
		return new EventNotifierImpl<T>(receiverHandler);
	}

}