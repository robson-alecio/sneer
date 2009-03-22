package wheel.reactive.sets.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import wheel.reactive.sets.SetChange;

public class SetValueChangeImpl<T> implements SetChange<T> {

	private final Collection<T> _elementsAdded;
	private final Collection<T> _elementsRemoved;

	public SetValueChangeImpl(T elementAdded, T elementRemoved) {
		this(wrap(elementAdded), wrap(elementRemoved)); //Optimize
	}

	public SetValueChangeImpl(Collection<T> added, Collection<T> removed) {
		_elementsAdded = nullToEmpty(added);
		_elementsRemoved = nullToEmpty(removed);
	}

	private static <CT> Collection<CT> wrap(CT element) {
		if (element == null) return null;
		Collection<CT> result = new ArrayList<CT>(1);
		result.add(element);
		return result;
	}

	private static <CT> Collection<CT> nullToEmpty(Collection<CT> collection) {
		return collection == null ? Collections.EMPTY_LIST : collection;
	}

	public Collection<T> elementsAdded() {
		return _elementsAdded;
	}

	public Collection<T> elementsRemoved() {
		return _elementsRemoved;
	}

}