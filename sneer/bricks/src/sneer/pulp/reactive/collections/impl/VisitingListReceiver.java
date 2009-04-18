package sneer.pulp.reactive.collections.impl;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.collections.ListChange;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ListChange.Visitor;

public abstract class VisitingListReceiver<T> implements Consumer<ListChange<T>>, Visitor<T> {

	protected final ListSignal<T> _input;

	public VisitingListReceiver(ListSignal<T> input) {
		_input = input;
		input.addReceiver(this);
	}

	@Override
	public void consume(ListChange<T> listChange) {
		listChange.accept(this);
	}

}
