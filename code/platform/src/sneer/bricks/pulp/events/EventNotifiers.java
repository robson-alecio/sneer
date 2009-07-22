package sneer.bricks.pulp.events;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface EventNotifiers {
	
	<T> EventNotifier<T> newInstance();

	/** @param receiverHandler will be called whenever a receiver is added to the returned EventNotifier. */
	<T> EventNotifier<T> newInstance(Consumer<Consumer<? super T>> receiverHandler);

}
