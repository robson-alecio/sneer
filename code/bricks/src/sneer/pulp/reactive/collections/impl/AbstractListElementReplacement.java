package sneer.pulp.reactive.collections.impl;

abstract class AbstractListElementReplacement<T> extends AbstractListValueChange<T> {

	protected final T _newElement;

	AbstractListElementReplacement(int index, T element, T newElement) {
		super(index, element);
		_newElement = newElement;
	}

}