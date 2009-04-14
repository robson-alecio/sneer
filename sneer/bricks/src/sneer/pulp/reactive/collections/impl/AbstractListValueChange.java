package sneer.pulp.reactive.collections.impl;

abstract class AbstractListValueChange<T> {

	protected final int _index;
	protected final T _element;
	
	AbstractListValueChange(int index, T element) {
		_index = index;
		_element = element;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " index:" + _index + " element:" + _element; 
	}

}