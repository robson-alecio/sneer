package wheel.io.ui.impl;

import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractListModel;

import wheel.io.ui.GuiThread;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.VisitingListReceiver;

public class ListSignalModel<T> extends AbstractListModel {

	public interface SignalChooser<E> {
		Signal<?>[] signalsToReceiveFrom(E element);
	}

	private final ListSignal<T> _input;
	private final Map<T, Receiver<?>> _elementReceivers = new HashMap<T, Receiver<?>>();
	private final SignalChooser<T> _chooser;
	@SuppressWarnings("unused")
	private final ListChangeReceiver _listReceiverToAvoidGc;

	public ListSignalModel(ListSignal<T> input, SignalChooser<T> chooser) {
		_input = input;
		_chooser = chooser;

		for (T element : input) 
			addReceiverToElement(element);
		
		_listReceiverToAvoidGc = new ListChangeReceiver(_input);
	}

	public ListSignalModel(ListSignal<T> input) {
		this(input, null);
	}

	private class ListChangeReceiver extends VisitingListReceiver<T> {

		private ListChangeReceiver(ListSignal<T> input) {
			super(input);
		}

		@Override
		public void elementAdded(final int index, T value) {
			addReceiverToElement(value);
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireIntervalAdded(ListSignalModel.this, index, index);
			}});		
		}

		@Override
		public void elementToBeRemoved(int index, T value) {
			removeReceiverFromElement(value);
		}

		@Override
		public void elementRemoved(final int index, T value) {
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireIntervalRemoved(ListSignalModel.this, index, index);
			}});		
		}

		@Override
		public void elementToBeReplaced(int index, T oldValue, T newValue) {
			removeReceiverFromElement(oldValue);
		}

		@Override
		public void elementReplaced(final int index, T oldValue, T newValue) {
			addReceiverToElement(newValue);
			contentsChanges(index);
		}

		@Override
		public void elementInserted(final int index, final T value) {
			addReceiverToElement(value);
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireIntervalAdded(ListSignalModel.this, index, index);
			}});		
		}
	}
	
	public int getSize() {
		Signal<Integer> size = _input.size();
		return size.currentValue();
	}
	
	public T getElementAt(int index) {
		return _input.currentGet(index);
	}

	private void removeReceiverFromElement(T element) {
		if (_chooser == null) return;

		_elementReceivers.remove(element).removeFromSignals();
	}

	private void addReceiverToElement(T element) {
		if (_chooser == null) return;

		Receiver<Object> receiver = createElementReceiver(element);
		_elementReceivers.put(element, receiver);
		
		for (Signal<?> signal : _chooser.signalsToReceiveFrom(element))
			receiver.addToSignal(signal);
	}

	private Receiver<Object> createElementReceiver(final T element) {
		return new Receiver<Object>() { public void consume(Object ignored) {
			int i = 0;
			for (T candidate : _input) {  //Optimize
				if (candidate == element)
					contentsChanges(i);
				i++;
			}}};
	}

	private void contentsChanges(final int index) {
		GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
			fireContentsChanged(ListSignalModel.this, index, index);
		}});
	}
	
	private static final long serialVersionUID = 1L;
}