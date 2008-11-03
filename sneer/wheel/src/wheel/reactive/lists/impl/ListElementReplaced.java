package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;


public final class ListElementReplaced<T> implements ListValueChange<T> {

	private final int _index;
	private final T _old;
	private final T _new;

	public ListElementReplaced(int index, T oldElement, T newElement) {
		_index = index;
		_old = oldElement;
		_new = newElement;
	}

	public void accept(Visitor<T> visitor) {
		visitor.elementReplaced(_index, _old, _new);
	}

	@Override
	public String toString() {
		return "List element replaced at " + _index;
	}
}
