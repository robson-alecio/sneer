package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;


public final class ListElementAdded implements ListValueChange {

	private final int _index;

	public ListElementAdded(int index) {
		_index = index;
	}

	public void accept(Visitor visitor) {
		visitor.elementAdded(_index);
	}

	@Override
	public String toString() {
		return "List element added at " + _index;
	}
}
