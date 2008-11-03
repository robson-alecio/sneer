package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;

final class ListElementToBeReplaced<T> extends AbstractListElementReplacement<T> implements ListValueChange<T> {

	ListElementToBeReplaced(int index, T oldElement, T newElement) {
		super(index, oldElement, newElement);
	}

	@Override
	public void accept(Visitor<T> visitor) {
		visitor.elementToBeReplaced(_index, _element, _newElement);
	}
}