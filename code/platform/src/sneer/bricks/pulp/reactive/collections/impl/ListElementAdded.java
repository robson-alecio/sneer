package sneer.bricks.pulp.reactive.collections.impl;

import java.util.Collection;

final class ListElementAdded<T> extends AbstractListValueChange<T> {

	ListElementAdded(int index, T element) {
		super(index, element);
	}

	@Override
	public void accept(Visitor<T> visitor) {
		visitor.elementAdded(_index, _element);
	}

	@Override
	public Collection<T> elementsAdded() {
		return newColection(_element);
	}
}