package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;


public final class ListElementAdded implements ListValueChange {

	private final int _index;
	private final Object _value;

	public ListElementAdded(int index, Object value) {
		_index = index;
		_value = value;
	}

	public void accept(Visitor visitor) {
		visitor.elementAdded(_index, _value);
	}

	@Override
	public String toString() {
		return "List element added at " + _index;
	}
}
