package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;


public final class ListElementRemoved implements ListValueChange {

	private final int _index;

	public ListElementRemoved(int index) {
		_index = index;
	}

	public void accept(Visitor visitor) {
		visitor.elementRemoved(_index);
	}

	@Override
	public String toString() {
		return "List element removed at " + _index;
	}
	
}
