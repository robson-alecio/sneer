package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;


public final class ListElementRemoved<T> implements ListValueChange<T> {

	private final int _index;
	private final T _element;

	public ListElementRemoved(int index, T tmp) {
		_index = index;
		_element = tmp;
	}

	public void accept(Visitor<T> visitor) {
		visitor.elementRemoved(_index, _element);
	}

	@Override
	public String toString() {
		return "List element removed at " + _index;
	}
}