package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;

public final class ListElementToBeReplaced<T> implements ListValueChange<T> {

	private final int _index;
	private final T _old;
	private final T _new;

	public ListElementToBeReplaced(int index, T oldElement, T newElement) {
		_index = index;
		_old = oldElement;
		_new = newElement;
	}

	public void accept(Visitor<T> visitor) {
		visitor.elementToBeReplaced(_index, _old, _new);
	}

}
