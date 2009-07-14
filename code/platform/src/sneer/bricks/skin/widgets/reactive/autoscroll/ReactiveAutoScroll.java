package sneer.bricks.skin.widgets.reactive.autoscroll;

import javax.swing.JScrollPane;

import sneer.bricks.pulp.events.EventSource;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface ReactiveAutoScroll {
	
	<T> JScrollPane create(EventSource<T> eventSource, Consumer<T> receiver);

}