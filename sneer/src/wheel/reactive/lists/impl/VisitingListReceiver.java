package wheel.reactive.lists.impl;

import wheel.reactive.Receiver;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;

public abstract class VisitingListReceiver implements Receiver<ListValueChange>, Visitor {

	@Override
	public void receive(ListValueChange listChange) {
		listChange.accept(this);
	}

}
