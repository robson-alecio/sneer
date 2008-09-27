package wheel.reactive.lists.impl;

import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;

public abstract class VisitingListReceiver<T> implements Omnivore<ListValueChange>, Visitor {

	protected final ListSignal<T> _input;

	public VisitingListReceiver(ListSignal<T> input) {
		_input = input;
		input.addListReceiver(this);
	}

	@Override
	public void consume(ListValueChange listChange) {
		listChange.accept(this);
	}

}
