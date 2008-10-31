package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListSignal;

public abstract class SimpleListReceiver<T> extends VisitingListReceiver<T> {
	
	public SimpleListReceiver(ListSignal<T> listSignal) {
		super(listSignal);
		for (T element : _input) elementPresent(element);
	}
	
	protected abstract void elementPresent(T element);
	protected abstract void elementAdded(T newElement);
	protected abstract void elementToBeRemoved(T element);

	@Override
	public void elementAdded(int index, Object value){
		elementAdded(_input.currentGet(index));
	}

	@Override
	public void elementToBeReplaced(int index) {
		elementToBeRemoved(index);
	}

	@Override
	public void elementReplaced(int index) {
		elementAdded(index, null);
	}

	@Override
	public void elementToBeRemoved(int index) {
		elementToBeRemoved(_input.currentGet(index));
	}

	@Override
	public void elementRemoved(int index) {		
	}
	


}
