package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListChange;

final class ListElementAdded<T> extends AbstractListValueChange<T> implements ListChange<T> {

	ListElementAdded(int index, T element) {
		super(index, element);
	}

	@Override
	public void accept(Visitor<T> visitor) {
		visitor.elementAdded(_index, _element);
	}
}