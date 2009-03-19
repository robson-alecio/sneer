package sneer.pulp.events.impl;

import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventNotifierFactory;
import wheel.lang.Consumer;

class EventNotifierFactoryImpl implements EventNotifierFactory {

	@Override
	public <T> EventNotifier<T> create() {
		return create(null);
	}

	@Override
	public <T> EventNotifier<T> create(Consumer<Consumer<? super T>> receiverHandler) {
		return new EventNotifierImpl<T>(receiverHandler);
	}

}