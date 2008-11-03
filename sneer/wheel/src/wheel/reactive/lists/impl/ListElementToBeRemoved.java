package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;


public final class ListElementToBeRemoved<T> implements ListValueChange<T> {

	private final int _index;
	private final T _element;

	public ListElementToBeRemoved(int index, T element) {
		_index = index;
		_element = element;
	}

	public void accept(Visitor<T> visitor) {
		visitor.elementToBeRemoved(_index, _element);
	}

	@Override
	public String toString() {
		return "List element removed at " + _index;
	}
}