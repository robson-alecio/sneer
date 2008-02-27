package wheel.reactive.lists.impl;

import wheel.lang.Omnivore;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;

public abstract class VisitingListReceiver implements Omnivore<ListValueChange>, Visitor {

	@Override
	public void consume(ListValueChange listChange) {
		listChange.accept(this);
	}

}
