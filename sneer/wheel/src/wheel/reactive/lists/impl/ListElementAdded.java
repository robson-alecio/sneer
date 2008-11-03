package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;


public final class ListElementAdded<T> implements ListValueChange<T> {

	private final int _index;
	private final T _element;

	public ListElementAdded(int index, T element) {
		_index = index;
		_element = element;
	}

	public void accept(Visitor<T> visitor) {
		visitor.elementAdded(_index, _element);
	}

	@Override
	public String toString() {
		return "List element added at " + _index;
	}
}