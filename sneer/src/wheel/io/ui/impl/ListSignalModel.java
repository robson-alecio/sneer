package wheel.io.ui.impl;

import javax.swing.AbstractListModel;

import sneer.kernel.business.contacts.Contact;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.impl.AbstractListReceiver;

public class ListSignalModel extends AbstractListModel {

	private Receiver<ListValueChange> _receiver = listChangeReceiver();
	private final ListSignal<?> _input;

	@SuppressWarnings("unchecked")
	public ListSignalModel(ListSignal<?> input){
		_input = input;
		_input.addListReceiver(_receiver);
	}

	protected Receiver<ListValueChange> listChangeReceiver() {
		return new ListChangeReceiver();
	}

	protected class ListChangeReceiver extends AbstractListReceiver {

		@Override
		public void listReplaced(int oldSize, int newSize) {
			if (newSize > oldSize) fireIntervalAdded(this, oldSize, newSize);
			if (newSize < oldSize) fireIntervalRemoved(this, newSize, oldSize);
			fireContentsChanged(this, 0, Math.min(oldSize, newSize));
		}
		
		@Override
		public void elementAdded(int index) {
			addReceiverToElement(getElementAt(index), index);
			fireIntervalAdded(this, index, index);
		}

		@Override
		public void elementToBeRemoved(int index) {}

		@Override
		public void elementRemoved(int index) {
			fireIntervalRemoved(this, index, index);
		}

		@Override
		public void elementReplaced(int index) {
			fireContentsChanged(this, index, index);
		}

	}
	
	public Object getElementAt(int index) {
		return _input.currentGet(index); //Optimize: get only necessary element, not entire list.
	}

	private void addReceiverToElement(Object element, int index) {
		Contact contact = (Contact)element;
		addReceiverToSignal(contact.isOnline(), index); //Optimize: use a single receiver for all signals maybe using Casts.uncheckedGenericCast();
		addReceiverToSignal(contact.state(), index);
		addReceiverToSignal(contact.nick(), index);
		addReceiverToSignal(contact.host(), index);
		addReceiverToSignal(contact.port(), index);
	}

	private <T> void addReceiverToSignal(Signal<T> signal, int index) {
		Receiver<T> r = elementReceiver(index);
		signal.addReceiver(r);
	}

	private <T> Receiver<T> elementReceiver(final int index) {
		return new Receiver<T>() {
			int _index = index;
			public void receive(T ignored) {
				fireContentsChanged(this, _index, _index);
				System.out.println("---------");
			}};
	}

	public int getSize() {
		return _input.currentSize();
	}
	
	private static final long serialVersionUID = 1L;

}
