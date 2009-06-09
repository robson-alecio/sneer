package sneer.bricks.pulp.events.impl;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;

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