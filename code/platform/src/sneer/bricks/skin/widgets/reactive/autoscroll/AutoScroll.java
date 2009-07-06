package sneer.bricks.skin.widgets.reactive.autoscroll;

import javax.swing.JScrollPane;

import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.CollectionSignal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface AutoScroll {
	
	void runWithAutoscroll(JScrollPane scrollPane, Runnable runnable);

	<T> JScrollPane create(EventSource<T> eventSource);
	<T> JScrollPane create(EventSource<T> eventSource, Consumer<T> receiver);
	<T> JScrollPane create(CollectionSignal<T> inputSignal, Consumer<CollectionChange<T>> receiver);

}