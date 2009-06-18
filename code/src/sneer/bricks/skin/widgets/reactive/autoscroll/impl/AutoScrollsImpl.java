package sneer.bricks.skin.widgets.reactive.autoscroll.impl;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.skin.widgets.reactive.autoscroll.AutoScrolls;
import sneer.foundation.lang.Consumer;

public class AutoScrollsImpl implements AutoScrolls {

	@Override
	public <T> JScrollPane create(JTextPane component, ListSignal<T> inputSignal, Consumer<CollectionChange<T>> receiver) {
		return new AutoScroll<T>(component, inputSignal, receiver).scrollPane();
	}

	@Override
	public <T> JScrollPane create(EventSource<T> eventSource) {
		return new AutoScroll<T>(eventSource).scrollPane();
	}
}
