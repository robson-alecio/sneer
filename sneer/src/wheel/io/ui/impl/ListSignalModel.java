package wheel.io.ui.impl;

import javax.swing.AbstractListModel;

import wheel.reactive.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;
import wheel.reactive.lists.impl.AbstractListReceiver;

public class ListSignalModel extends AbstractListModel {

	private Receiver<ListValueChange> _receiver = new ListChangeReceiver();
	private final ListSignal<?> _input;

	@SuppressWarnings("unchecked")
	public ListSignalModel(ListSignal<?> input){
		_input = input;
		_input.addListReceiver(_receiver);
	}

	private class ListChangeReceiver extends AbstractListReceiver {

		@Override
		public void listReplaced(int oldSize, int newSize) {
			if (newSize > oldSize) fireIntervalAdded(this, oldSize, newSize);
			if (newSize < oldSize) fireIntervalRemoved(this, newSize, oldSize);
			fireContentsChanged(this, 0, Math.min(oldSize, newSize));
		}
		
		@Override
		public void elementAdded(int index) {
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

	public int getSize() {
		return _input.currentSize();
	}
	
	private static final long serialVersionUID = 1L;

}
