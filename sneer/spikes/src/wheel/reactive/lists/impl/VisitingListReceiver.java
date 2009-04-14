package wheel.reactive.lists.impl;

import wheel.lang.Consumer;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListChange;
import wheel.reactive.lists.ListChange.Visitor;

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
