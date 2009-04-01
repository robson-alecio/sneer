package sneer.pulp.events;

import sneer.brickness.OldBrick;
import wheel.lang.Consumer;

public interface EventNotifiers extends OldBrick {
	
	<T> EventNotifier<T> create();

	/** @param receiverHandler will be called whenever a receiver is added to the returned EventNotifier. */
	<T> EventNotifier<T> create(Consumer<Consumer<? super T>> receiverHandler);

}
