package wheel.io.ui.impl;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

import sneer.kernel.business.contacts.Contact;
import wheel.lang.Casts;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.impl.VisitingListReceiver;

public class ListSignalModel extends AbstractListModel {

	private final ListSignal<?> _input;
	private final List<Receiver<?>> _elementReceivers = new LinkedList<Receiver<?>>();

	@SuppressWarnings("unchecked")
	public ListSignalModel(ListSignal<?> input){
		_input = input;
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
		Contact contact = (Contact)getElementAt(index);
		Receiver<?> receiver = _elementReceivers.remove(index);

		removeReceiverFromSignal(receiver, contact.isOnline());
		removeReceiverFromSignal(receiver, contact.state());
		removeReceiverFromSignal(receiver, contact.nick());
		removeReceiverFromSignal(receiver, contact.host());
		removeReceiverFromSignal(receiver, contact.port());
	}

	private void addReceiverToElement(int index) {
		Contact contact = (Contact)getElementAt(index); //Fix: Make generic, not only for Contact.
		Receiver<?> receiver = createElementReceiver(index);
		_elementReceivers.add(index, receiver);
		
		addReceiverToSignal(receiver, contact.isOnline());
		addReceiverToSignal(receiver, contact.state());
		addReceiverToSignal(receiver, contact.nick());
		addReceiverToSignal(receiver, contact.host());
		addReceiverToSignal(receiver, contact.port());
	}

	private <T> void addReceiverToSignal(Receiver<?> receiver, Signal<T> signal) {
		Receiver<T> castedReceiver =	Casts.uncheckedGenericCast(receiver);
		signal.addReceiver(castedReceiver);
	}
	
	private <T> void removeReceiverFromSignal(Receiver<?> receiver, Signal<T> signal) {
		Receiver<T> casted = Casts.uncheckedGenericCast(receiver);
		signal.removeReceiver(casted);
	}

	private <T> Receiver<T> createElementReceiver(final int index) {
		return new Receiver<T>() {
			int _index = index;
			public void receive(T ignored) {
				System.out.println("element receiver notified");
				fireContentsChanged(this, _index, _index);
			}
		};
	}

	private static final long serialVersionUID = 1L;

}
