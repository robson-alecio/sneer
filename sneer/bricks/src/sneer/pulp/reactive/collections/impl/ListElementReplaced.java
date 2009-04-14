package sneer.pulp.reactive.collections.impl;

import wheel.reactive.lists.ListChange;

final class ListElementReplaced<T> extends AbstractListElementReplacement<T> implements ListChange<T> {

	ListElementReplaced(int index, T oldElement, T newElement) {
		super(index, oldElement, newElement);
	}

	@Override
	public void accept(Visitor<T> visitor) {
		visitor.elementReplaced(_index, _element, _newElement);
	}
}