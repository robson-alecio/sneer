package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;


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
