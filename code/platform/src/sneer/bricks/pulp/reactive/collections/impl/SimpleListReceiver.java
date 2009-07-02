package sneer.bricks.pulp.reactive.collections.impl;

import sneer.bricks.pulp.reactive.collections.ListSignal;

public abstract class SimpleListReceiver<T> extends VisitingListReceiver<T> {
	
	public SimpleListReceiver(ListSignal<T> listSignal) {
		super(listSignal);
		for (T element : listSignal) elementPresent(element);
	}
	
	protected abstract void elementPresent(T element);
	protected abstract void elementAdded(T newElement);
	protected abstract void elementRemoved(T element);

	@Override
	public void elementAdded(int index, T value){
		elementAdded(value);
	}

	@Override
	public void elementReplaced(int index, T oldValue, T newValue) {
		elementRemoved(oldValue);
		elementAdded(newValue);
	}

	@Override
	public void elementRemoved(int index, T value) {		
		elementRemoved(value);
	}

	@Override
	public void elementMoved(int index, int newIndex, T newElement) {
		//I dont care.
	}
}