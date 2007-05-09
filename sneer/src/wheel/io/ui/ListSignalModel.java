package wheel.io.ui;

import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;

import sneer.kernel.business.Contact;

import wheel.reactive.Receiver;
import wheel.reactive.list.ListSignal;
import wheel.reactive.list.ListSignal.ListValueChange;
import wheel.reactive.list.ListSignal.ListValueChangeVisitor;

public class ListSignalModel extends AbstractListModel {

	private class MyReceiver implements Receiver<ListValueChange> {

		public void receive(ListValueChange valueChange) {
			valueChange.accept(getListModelValueChangeVisitor());
		}

	}

	private ListModelValueChangeVisitor _listModelVisitor;
	
	private Receiver<ListValueChange> _receiver = new MyReceiver();
	private final ListSignal<?> _input;

	private int _currentListSize = 0;
	
	@SuppressWarnings("unchecked")
	public ListSignalModel(ListSignal<?> input){
		_input = input;
		_input.addListReceiver(_receiver);
	}

	private ListModelValueChangeVisitor getListModelValueChangeVisitor() {
		if (_listModelVisitor == null)
			_listModelVisitor = new ListModelValueChangeVisitor();
		return _listModelVisitor;
	}

	private class ListModelValueChangeVisitor implements ListValueChangeVisitor {

		public void listReplaced() {
			fireIntervalRemoved(this, 0, _currentListSize );
			_currentListSize = _input.currentValue().size(); //Optimize: get only size, not entire list.
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
		return _input.currentValue().get(index); //Optimize: get only necessary element, not entire list.
	}

	public int getSize() {
		return _currentListSize;
	}
	
	private static final long serialVersionUID = 1L;

}
