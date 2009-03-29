package sneer.skin.widgets.reactive.impl;

import static sneer.commons.environments.Environments.my;

import javax.swing.AbstractListModel;

import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.pulp.reactive.signalchooser.SignalChooserManagerFactory;
import wheel.io.ui.GuiThread;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.VisitingListReceiver;

class ListSignalModel<T> extends AbstractListModel {

	private final SignalChooserManagerFactory _signalChooserManagerFactory = my(SignalChooserManagerFactory.class);
	
	private final ListSignal<T> _input;
	
	@SuppressWarnings("unused") private final Object _refToAvoidGc;

	private final SignalChooser<T> _chooser;
	
	ListSignalModel(ListSignal<T> input, SignalChooser<T> chooser) {
		_input = input;
		_chooser = chooser;
		_refToAvoidGc = _signalChooserManagerFactory.newManager(input, new ModelChangeReceiver(_input));
	}
	
	private class ModelChangeReceiver extends VisitingListReceiver<T> implements ListOfSignalsReceiver<T> {

		private ModelChangeReceiver(ListSignal<T> input) {
			super(input);
		}

		@Override
		public void elementAdded(final int index, T value) {
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireIntervalAdded(ListSignalModel.this, index, index);
			}});		
		}

		@Override
		public void elementRemoved(final int index, T value) {
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireIntervalRemoved(ListSignalModel.this, index, index);
			}});		
		}

		@Override
		public void elementReplaced(final int index, T oldValue, T newValue) {
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				contentsChanged(index);
			}});
		}

		@Override
		public void elementInserted(final int index, final T value) {
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireIntervalAdded(ListSignalModel.this, index, index);
			}});		
		}

		@Override
		public void elementMoved(final int oldIndex, final int newIndex, final T value) {
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				fireContentsChanged(ListSignalModel.this, oldIndex, newIndex);
			}});
		}

		@Override
		public void elementSignalChanged(final int index, final T value) {
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				elementChanged(value);
			}});			
		}

		@Override
		public SignalChooser<T> signalChooser() {
			return _chooser;
		}
	}
	
	@Override
	public int getSize() {
		Signal<Integer> size = _input.size();
		return size.currentValue();
	}
	
	@Override
	public T getElementAt(int index) {
		return _input.currentGet(index);
	}

	private void contentsChanged(final int index) {
		fireContentsChanged(ListSignalModel.this, index, index);
	}
	
	private void elementChanged(T element) {
		int i = 0;
		for (T candidate : _input) {  //Optimize
			if (candidate == element)
				contentsChanged(i);
			i++;
		}
	}	

	private static final long serialVersionUID = 1L;
}