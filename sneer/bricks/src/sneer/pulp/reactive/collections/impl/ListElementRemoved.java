package sneer.pulp.reactive.collections.impl;

import java.util.Collection;
import java.util.Collections;

final class ListElementRemoved<T> extends AbstractListValueChange<T> {

	ListElementRemoved(int index, T element) {
		super(index, element);
	}

	@Override
	public void accept(Visitor<T> visitor) {
		visitor.elementRemoved(_index, _element);
	}

	@Override
	public Collection<T> elementsAdded() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Collection<T> elementsRemoved() {
		return newColection(_element);
	}
}