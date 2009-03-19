package sneer.pulp.events;

import sneer.brickness.Brick;
import wheel.lang.Consumer;

public interface EventNotifierFactory extends Brick {
	
	<T> EventNotifier<T> create();

	/** @param receiverHandler will be called whenever a receiver is added to the returned EventNotifier. */
	<T> EventNotifier<T> create(Consumer<Consumer<? super T>> receiverHandler);

}
