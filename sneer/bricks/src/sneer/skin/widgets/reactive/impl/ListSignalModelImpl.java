package sneer.skin.widgets.reactive.impl;

import javax.swing.AbstractListModel;

import sneer.kernel.container.Inject;
import sneer.pulp.reactive.signalchooser.ElementsObserverFactory;
import sneer.pulp.reactive.signalchooser.ElementsObserverFactory.ElementsObserver;
import sneer.pulp.reactive.signalchooser.ElementsObserverFactory.SignalChooser;
import sneer.skin.widgets.reactive.ListSignalModel;
import wheel.io.ui.GuiThread;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.VisitingListReceiver;

public class ListSignalModelImpl<T> extends AbstractListModel implements ListSignalModel<T>{

	private final ListSignal<T> _input;
	
	@Inject
	private static ElementsObserverFactory _contentManager;
	private ElementsObserver<T> _elementsObserver;
	
	@SuppressWarnings("unused")
	private ListChangeReceiver _listReceiverToAvoidGc;
	
	ListSignalModelImpl(ListSignal<T> input, SignalChooser<T> chooser) {
		_input = input;
		initModel(chooser);
	}

	private void initModel(SignalChooser<T> chooser) {
		_elementsObserver = _contentManager.newObserver(chooser, this);

		for (T element : _input)
			_elementsObserver.elementAdded(element);

		_listReceiverToAvoidGc = new ListChangeReceiver(_input);
	}

	private class ListChangeReceiver extends VisitingListReceiver<T> {

		private ListChangeReceiver(ListSignal<T> input) {
			super(input);
		}

		@Override public void elementToBeRemoved(int index, T value) { /*ignore*/	}
		@Override public void elementToBeReplaced(int index, T oldValue, T newValue) { /*ignore*/	}

		@Override
		public void elementAdded(final int index, T value) {
			_elementsObserver.elementAdded(value);
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireIntervalAdded(ListSignalModelImpl.this, index, index);
			}});		
		}

		@Override
		public void elementRemoved(final int index, T value) {
			_elementsObserver.elementRemoved(value);
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireIntervalRemoved(ListSignalModelImpl.this, index, index);
			}});		
		}

		@Override
		public void elementReplaced(final int index, T oldValue, T newValue) {
			_elementsObserver.elementRemoved(oldValue);
			_elementsObserver.elementAdded(newValue);
			contentsChanged(index);
		}

		@Override
		public void elementInserted(final int index, final T value) {
			_elementsObserver.elementAdded(value);
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireIntervalAdded(ListSignalModelImpl.this, index, index);
			}});		
		}

		@Override
		public void elementMoved(final int oldIndex, final int newIndex, T element) {
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireContentsChanged(ListSignalModelImpl.this, oldIndex, newIndex);
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

	private void contentsChanged(final int index) {
		GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
			fireContentsChanged(ListSignalModelImpl.this, index, index);
		}});
	}
	
	@Override
	public void elementChanged(T element) {
		int i = 0;
		for (T candidate : _input) {  //Optimize
			if (candidate == element)
				contentsChanged(i);
			i++;
		}
	}
	
	private static final long serialVersionUID = 1L;
}