package sneer.pulp.reactive.collections.impl;

import sneer.pulp.reactive.collections.ListChange;

final class ListElementAdded<T> extends AbstractListValueChange<T> implements ListChange<T> {

	ListElementAdded(int index, T element) {
		super(index, element);
	}

	@Override
	public void accept(Visitor<T> visitor) {
		visitor.elementAdded(_index, _element);
	}
}