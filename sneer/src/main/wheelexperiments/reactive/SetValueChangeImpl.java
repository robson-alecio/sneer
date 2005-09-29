package wheelexperiments.reactive;

import java.util.ArrayList;
import java.util.Collection;

import wheelexperiments.reactive.signals.SetSignal.SetValueChange;

public class SetValueChangeImpl<T> implements SetValueChange<T> {

	private final Collection<T> _elementsAdded;
	private final Collection<T> _elementsRemoved;

	public SetValueChangeImpl(T elementAdded, T elementRemoved) {
		_elementsAdded = new ArrayList<T>(1);
		_elementsAdded.add(elementAdded);
		_elementsRemoved = new ArrayList<T>(1);
		_elementsRemoved.add(elementRemoved);
	}

	public SetValueChangeImpl(Collection<T> added, Collection<T> removed) {
		_elementsAdded = added;
		_elementsRemoved = removed;
	}

	public Collection<T> elementsAdded() {
		return _elementsAdded;
	}

	public Collection<T> elementsRemoved() {
		return _elementsRemoved;
	}

}