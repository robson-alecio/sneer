package wheel.io.ui.impl;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.VisitingListReceiver;

public class ListSignalModel<T> extends AbstractListModel {

	public interface SignalChooser<E> {
		Signal<?>[] signalsToReceiveFrom(E element);
	}

	private final ListSignal<T> _input;
	private final List<Receiver<?>> _elementReceivers = new LinkedList<Receiver<?>>();
	private final SignalChooser<T> _chooser;
	private final ListChangeReceiver _listReceiverToAvoidGc = new ListChangeReceiver();

	public ListSignalModel(ListSignal<T> input, SignalChooser<T> chooser) {
		_input = input;
		_chooser = chooser;

		int size = _input.currentSize();
		for (int i = 0; i < size; i++) addReceiverToElement(i);

		_input.addListReceiver(_listReceiverToAvoidGc);
	}

	public ListSignalModel(ListSignal<T> input) {
		this(input, null);
	}

	private class ListChangeReceiver extends VisitingListReceiver {

		@Override
		public void elementAdded(int index) {
			addReceiverToElement(index);
			fireIntervalAdded(ListSignalModel.this, index, index);
		}

		@Override
		public void elementToBeRemoved(int index) {
			removeReceiverFromElement(index);
		}

		@Override
		public void elementRemoved(int index) {
			fireIntervalRemoved(ListSignalModel.this, index, index);
		}

		@Override
		public void elementToBeReplaced(int index) {
			removeReceiverFromElement(index);
		}

		@Override
		public void elementReplaced(int index) {
			addReceiverToElement(index);
			fireContentsChanged(ListSignalModel.this, index, index);
		}

	}
	
	public int getSize() {
		return _input.currentSize();
	}
	
	public T getElementAt(int index) {
		return _input.currentGet(index);
	}

	private void removeReceiverFromElement(int index) {
		if (_chooser == null) return;

		_elementReceivers.remove(index).removeFromSignals();
	}

	private void addReceiverToElement(int index) {
		if (_chooser == null) return;

		T element = getElementAt(index);
		Receiver<Object> receiver = createElementReceiver(element);
		_elementReceivers.add(index, receiver);
		
		for (Signal<?> signal : _chooser.signalsToReceiveFrom(element))
			receiver.addToSignal(signal);
	}

	private Receiver<Object> createElementReceiver(final T element) {
		return new Receiver<Object>() { public void consume(Object ignored) {
			int i = 0;
			for (T candidate : _input) {  //Optimize
				if (candidate == element)
					fireContentsChanged(ListSignalModel.this, i, i);
				i++;
			}}};
	}

	private static final long serialVersionUID = 1L;
}
