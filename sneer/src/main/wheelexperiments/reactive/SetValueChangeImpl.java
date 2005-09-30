package wheelexperiments.reactive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import wheelexperiments.reactive.SetSignal.SetValueChange;

public class SetValueChangeImpl<T> implements SetValueChange<T>, Serializable {

	private final Collection<T> _elementsAdded;
	private final Collection<T> _elementsRemoved;

	public SetValueChangeImpl(T elementAdded, T elementRemoved) {
		_elementsAdded = new ArrayList<T>(1);
		_elementsAdded.add(elementAdded);
		_elementsRemoved = new ArrayList<T>(1);
		_elementsRemoved.add(elementRemoved);
	}

	public SetValueChangeImpl(Collection<T> added, Collection<T> removed) {
		_elementsAdded = nullToEmpty(added);
		_elementsRemoved = nullToEmpty(removed);
	}

	private Collection<T> nullToEmpty(Collection<T> collection) {
		return collection == null ? Collections.EMPTY_LIST : collection;
	}

	public Collection<T> elementsAdded() {
		return _elementsAdded;
	}

	public Collection<T> elementsRemoved() {
		return _elementsRemoved;
	}

	private static final long serialVersionUID = 1L;
}