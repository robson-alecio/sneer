package wheel.reactive.lists.impl;

import sneer.kernel.business.contacts.Contact;
import wheel.reactive.lists.ListSignal;

public abstract class SimpleListReceiver<T> extends VisitingListReceiver {
	
	public SimpleListReceiver(ListSignal<T> listSignal) {
		_listSignal = listSignal;
		for (T element : _listSignal) elementPresent(element);
		_listSignal.addListReceiver(this);
	}

	private final ListSignal<T> _listSignal;

	protected abstract void elementPresent(T element);
	protected abstract void elementAdded(T newElement);
	protected abstract void elementToBeRemoved(T element);

	@Override
	public void elementAdded(int index){
		elementAdded(_listSignal.currentGet(index));
	}

	@Override
	public void elementToBeReplaced(int index) {
		elementToBeRemoved(index);
	}

	@Override
	public void elementReplaced(int index) {
		elementAdded(index);
	}

	@Override
	public void elementToBeRemoved(int index) {
		elementToBeRemoved(_listSignal.currentGet(index));
	}

	@Override
	public void elementRemoved(int index) {		
	}
	


}
