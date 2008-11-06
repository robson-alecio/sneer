package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListSignal;

public abstract class SimpleListReceiver<T> extends VisitingListReceiver<T> {
	
	public SimpleListReceiver(ListSignal<T> listSignal) {
		super(listSignal);
		for (T element : _input) elementPresent(element);
	}
	
	protected abstract void elementMovedTo(T element, int newIndex);
	protected abstract void elementPresent(T element);
	protected abstract void elementAdded(T newElement);
	protected abstract void elementToBeRemoved(T element);

	@Override
	public void elementMoved(int oldIndex, int newIndex, T element) {
		elementMovedTo(element, newIndex);
	}	
	
	@Override
	public void elementAdded(int index, T value){
		elementAdded(value);
	}

	@Override
	public void elementInserted(int index, T value){
		elementAdded(value);
	}

	@Override
	public void elementToBeReplaced(int index, T oldValue, T newValue) {
		elementToBeRemoved(index, newValue);
	}

	@Override
	public void elementReplaced(int index, T oldValue, T newValue) {
		elementAdded(index, newValue);
	}

	@Override
	public void elementToBeRemoved(int index, T value) {
		elementToBeRemoved(value);
	}
	
	@Override
	public void elementRemoved(int index, T value) {		
	}
}