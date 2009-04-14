package sneer.pulp.events;

import sneer.brickness.Brick;
import sneer.software.lang.Consumer;

@Brick
public interface EventNotifiers {
	
	<T> EventNotifier<T> create();

	/** @param receiverHandler will be called whenever a receiver is added to the returned EventNotifier. */
	<T> EventNotifier<T> create(Consumer<Consumer<? super T>> receiverHandler);

}
