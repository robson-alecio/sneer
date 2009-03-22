package wheel.reactive.lists.impl;

import wheel.lang.Consumer;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;

public abstract class VisitingListReceiver<T> implements Consumer<ListValueChange<T>>, Visitor<T> {

	protected final ListSignal<T> _input;

	public VisitingListReceiver(ListSignal<T> input) {
		_input = input;
		input.addReceiver(this);
	}

	@Override
	public void consume(ListValueChange<T> listChange) {
		listChange.accept(this);
	}

}
