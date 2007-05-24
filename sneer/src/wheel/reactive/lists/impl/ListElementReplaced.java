package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;


public final class ListElementReplaced implements ListValueChange {

	private final int _index;

	public ListElementReplaced(int index) {
		_index = index;
	}

	public void accept(Visitor visitor) {
		visitor.elementReplaced(_index);
	}

	@Override
	public String toString() {
		return "List element replaced at " + _index;
	}
	
}
