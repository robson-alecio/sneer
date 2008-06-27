package sneer.games.mediawars.mp3sushi.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.VisitingListReceiver;

public class ListSignalModel<T> extends AbstractListModel {

	private final ListSignal<T> _input;
	private final List<Omnivore<T>> _elementReceivers = new LinkedList<Omnivore<T>>();
	private ReceiverManager<T> _receiverManager;

	public ListSignalModel(ListSignal<T> input, ReceiverManager<T> receiverManager){
		_receiverManager = receiverManager;
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
	

	public T getElementAt(int index) {
		return _input.currentGet(index);
	}

	private void removeReceiverFromElement(int index) {
		Omnivore<T> receiver = _elementReceivers.remove(index);
		_receiverManager.removeReceiverFromSignals(receiver, getElementAt(index));
	}

	private void addReceiverToElement(int index) {
		Omnivore<T> receiver = createElementReceiver(index);
		_elementReceivers.add(index, receiver);
		_receiverManager.addReceiverToSignals(receiver, getElementAt(index));
		
	}


	private Omnivore<T> createElementReceiver(final int index) {
		return new Omnivore<T>() {
			int _index = index;
			public void consume(T ignored) {
				fireContentsChanged(this, _index, _index);
			}
		};
	}

	private static final long serialVersionUID = 1L;

}
