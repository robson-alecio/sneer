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

	private int _currentListSize = 0;
	
	@SuppressWarnings("unchecked")
	public ListSignalModel(ListSignal<?> input){
		_input = input;
		_input.addListReceiver(_receiver);
	}

	private class ListChangeReceiver extends AbstractListReceiver {

		public void listReplaced() {
			fireIntervalRemoved(this, 0, _currentListSize );
			_currentListSize = _input.currentSize(); //Optimize: get only size, not entire list.
			fireContentsChanged(this, 0, _currentListSize);
		}
		
		public void elementAdded(int index) {
			_currentListSize++;
			fireIntervalAdded(this, index, index);
		}

		public void elementRemoved(int index) {
			_currentListSize--;
			fireIntervalRemoved(this, index, index);
		}

		public void elementReplaced(int index) {
			fireContentsChanged(this, index, index);
		}
	
	}
	
	public Object getElementAt(int index) {
		return _input.currentGet(index); //Optimize: get only necessary element, not entire list.
	}

	public int getSize() {
		return _currentListSize;
	}
	
	private static final long serialVersionUID = 1L;

}
