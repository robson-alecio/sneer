package sneer.bricks.pulp.reactive.collections.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.ListChange;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.foundation.lang.Consumer;

abstract class AbstractListSignal<T> implements ListSignal<T> {

	EventNotifier<ListChange<T>> _notifierAsList = my(EventNotifiers.class).newInstance(new Consumer<Consumer<? super ListChange<T>>>(){@Override public void consume(Consumer<? super ListChange<T>> receiver) {
		//TODO
	}});

	EventNotifier<CollectionChange<T>> _notifierAsCollection = my(EventNotifiers.class).newInstance(new Consumer<Consumer<? super CollectionChange<T>>>(){@Override public void consume(Consumer<? super CollectionChange<T>> receiver) {
		//TODO
	}});

	void notifyReceivers(final AbstractListValueChange<T> valueChange) {
		_notifierAsList.notifyReceivers(valueChange);
		_notifierAsCollection.notifyReceivers(valueChange);
	}

	@Override
	public WeakContract addPulseReceiver(Runnable pulseReceiver) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public WeakContract addListReceiver(Consumer<? super ListChange<T>> receiver) {
		return _notifierAsList.output().addReceiver(receiver);
	}

	@Override
	public WeakContract addReceiver(Consumer<? super CollectionChange<T>> receiver) {
		return _notifierAsCollection.output().addReceiver(receiver);
	}
}
