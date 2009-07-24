package sneer.bricks.pulp.events.impl;

import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.events.Pulser;
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

	@Override
	public Pulser newPulser() {
		return new PulserImpl();
	}

}