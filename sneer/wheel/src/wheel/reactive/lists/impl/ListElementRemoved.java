package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;

final class ListElementRemoved<T> extends AbstractListValueChange<T> implements ListValueChange<T> {

	ListElementRemoved(int index, T element) {
		super(index, element);
	}

	@Override
	public void accept(Visitor<T> visitor) {
		visitor.elementRemoved(_index, _element);
	}
}