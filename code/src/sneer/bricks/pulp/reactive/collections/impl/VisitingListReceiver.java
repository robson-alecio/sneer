package sneer.bricks.pulp.reactive.collections.impl;

import sneer.bricks.pulp.reactive.collections.ListChange;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.collections.ListChange.Visitor;
import sneer.foundation.lang.Consumer;

public abstract class VisitingListReceiver<T> implements Consumer<ListChange<T>>, Visitor<T> {

	protected final ListSignal<T> _input;

	public VisitingListReceiver(ListSignal<T> input) {
		_input = input;
		input.addListReceiver(this);
	}

	@Override
	public void consume(ListChange<T> listChange) {
		listChange.accept(this);
	}
}
