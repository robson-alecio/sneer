package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;

final class ListElementToBeRemoved<T> extends AbstractListValueChange<T> implements ListValueChange<T> {

	ListElementToBeRemoved(int index, T element) {
		super(index, element);
	}

	@Override
	public void accept(Visitor<T> visitor) {
		visitor.elementToBeRemoved(_index, _element);
	}
}