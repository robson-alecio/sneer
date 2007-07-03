package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;


public final class ListElementToBeRemoved implements ListValueChange {

	private final int _index;

	public ListElementToBeRemoved(int index) {
		_index = index;
	}

	public void accept(Visitor visitor) {
		visitor.elementToBeRemoved(_index);
	}

	@Override
	public String toString() {
		return "List element removed at " + _index;
	}
	
}
