package sneer.pulp.reactive.collections.impl;

import java.util.Collection;
import java.util.Collections;

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

	@Override
	public Collection<T> elementsRemoved() {
		return Collections.EMPTY_LIST;
	}
}