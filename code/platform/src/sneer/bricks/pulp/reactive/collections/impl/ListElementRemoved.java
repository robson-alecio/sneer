package sneer.bricks.pulp.reactive.collections.impl;

import java.util.Collection;

final class ListElementRemoved<T> extends AbstractListValueChange<T> {

	ListElementRemoved(int index, T element) {
		super(index, element);
	}

	@Override
	public void accept(Visitor<T> visitor) {
		visitor.elementRemoved(_index, _element);
	}

	@Override
	public Collection<T> elementsRemoved() {
		return newColection(_element);
	}
}