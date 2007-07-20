package wheel.io.ui.impl;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

import sneer.kernel.business.contacts.ContactAttributes;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.VisitingListReceiver;

public class ListSignalModel extends AbstractListModel {

	private final ListSignal<?> _input;
	private final List<Omnivore<?>> _elementReceivers = new LinkedList<Omnivore<?>>();

	@SuppressWarnings("unchecked")
	public ListSignalModel(ListSignal<?> input){
		_input = input;
		
		int size = _input.currentSize();
		for (int i = 0; i < size; i++) addReceiverToElement(i);
			
		_input.addListReceiver(new ListChangeReceiver());
	}

	private class ListChangeReceiver extends VisitingListReceiver {

		@Override
		public void elementAdded(int index) {
			addReceiverToElement(index);
			fireIntervalAdded(this, index, index);
		}

		@Override
		public void elementToBeRemoved(int index) {
			removeReceiverFromElement(index);
		}

		@Override
		public void elementRemoved(int index) {
			fireIntervalRemoved(this, index, index);
		}

		@Override
		public void elementToBeReplaced(int index) {
			removeReceiverFromElement(index);
		}

		@Override
		public void elementReplaced(int index) {
			addReceiverToElement(index);
			fireContentsChanged(this, index, index);
		}

	}
	
	public int getSize() {
		return _input.currentSize();
	}
	
	public Object getElementAt(int index) {
		return _input.currentGet(index);
	}

	private void removeReceiverFromElement(int index) {
		ContactAttributes contact = (ContactAttributes)getElementAt(index);
		Omnivore<?> receiver = _elementReceivers.remove(index);

		removeReceiverFromSignal(receiver, contact.isOnline());
		removeReceiverFromSignal(receiver, contact.state());
		removeReceiverFromSignal(receiver, contact.nick());
		removeReceiverFromSignal(receiver, contact.host());
		removeReceiverFromSignal(receiver, contact.port());
	}

	private void addReceiverToElement(int index) {
		ContactAttributes contact = (ContactAttributes)getElementAt(index); //Fix: Make generic, not only for Contact.
		Omnivore<?> receiver = createElementReceiver(index);
		_elementReceivers.add(index, receiver);
		
		addReceiverToSignal(receiver, contact.isOnline());
		addReceiverToSignal(receiver, contact.state());
		addReceiverToSignal(receiver, contact.nick());
		addReceiverToSignal(receiver, contact.host());
		addReceiverToSignal(receiver, contact.port());
	}

	private <T> void addReceiverToSignal(Omnivore<?> receiver, Signal<T> signal) {
		Omnivore<T> castedReceiver = Casts.uncheckedGenericCast(receiver);
		signal.addReceiver(castedReceiver);
	}
	
	private <T> void removeReceiverFromSignal(Omnivore<?> receiver, Signal<T> signal) {
		Omnivore<T> casted = Casts.uncheckedGenericCast(receiver);
		signal.removeReceiver(casted);
	}

	private <T> Omnivore<T> createElementReceiver(final int index) {
		return new Omnivore<T>() {
			int _index = index;
			public void consume(T ignored) {
				fireContentsChanged(this, _index, _index);
			}
		};
	}

	private static final long serialVersionUID = 1L;

}
