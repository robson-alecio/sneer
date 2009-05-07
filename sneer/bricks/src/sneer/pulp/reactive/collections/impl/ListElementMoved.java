package sneer.pulp.reactive.collections.impl;

import java.util.Collection;
import java.util.Collections;

final class ListElementMoved<T> extends AbstractListValueChange<T> {

	private final int _newIndex;

	ListElementMoved(int oldIndex, int newIndex, T element) {
		super(oldIndex, element);
		_newIndex = newIndex;
	}

	@Override
	public void accept(Visitor<T> visitor) {
		visitor.elementMoved(_index, _newIndex, _element);
	}

	@Override
	public Collection<T> elementsAdded() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Collection<T> elementsRemoved() {
		return Collections.EMPTY_LIST;
	}
}