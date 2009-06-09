package sneer.skin.widgets.reactive.autoscroll.impl;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.events.EventSource;
import sneer.pulp.reactive.collections.CollectionChange;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.skin.widgets.reactive.autoscroll.AutoScrolls;

public class AutoScrollsImpl implements AutoScrolls {

	@Override
	public <T> JScrollPane create(JTextPane component, ListSignal<T> inputSignal, Consumer<CollectionChange<T>> receiver) {
		return new AutoScroll<T>(component, inputSignal, receiver);
	}

	@Override
	public <T> JScrollPane create(EventSource<T> eventSource) {
		return new AutoScroll<T>(eventSource);
	}

}
