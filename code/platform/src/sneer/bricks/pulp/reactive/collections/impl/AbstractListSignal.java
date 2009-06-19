package sneer.bricks.pulp.reactive.collections.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.ListChange;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.foundation.lang.Consumer;

abstract class AbstractListSignal<T> implements ListSignal<T> {

	EventNotifier<ListChange<T>> _notifier = my(EventNotifiers.class).create(new Consumer<Consumer<? super ListChange<T>>>(){@Override public void consume(Consumer<? super ListChange<T>> receiver) {
		//TODO
	}});

	EventNotifier<CollectionChange<T>> _notifier2 = my(EventNotifiers.class).create(new Consumer<Consumer<? super CollectionChange<T>>>(){@Override public void consume(Consumer<? super CollectionChange<T>> receiver) {
		//TODO
	}});

	void notifyReceivers(final AbstractListValueChange<T> valueChange) {
		_notifier.notifyReceivers(valueChange);
		_notifier2.notifyReceivers(valueChange);
	}

	@Override
	public void addListReceiver(Consumer<? super ListChange<T>> receiver) {
		_notifier.output().addReceiver(receiver);
	}

	@Override
	public void removeListReceiver(Object receiver) {
		_notifier.output().removeReceiver(receiver);	
	}

	@Override
	public void removeReceiver(Object receiver) {
		_notifier2.output().removeReceiver(receiver);
	}

	@Override
	public void addReceiver(Consumer<? super CollectionChange<T>> receiver) {
		_notifier2.output().addReceiver(receiver);
	}
}
