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

	private List<String> _list;
	private ListModelValueChangeVisitor _listModelVisitor;
	
	private Receiver<ListValueChange> _receiver = new MyReceiver();
	private final ListSignal<String> _input;
	
	@SuppressWarnings("unchecked")
	public ListSignalModel(ListSignal<String> input){
		_list = Collections.EMPTY_LIST;
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
			fireIntervalRemoved(this, 0, _list.size());
			_list = _input.currentValue();
			fireContentsChanged(this, 0, _list.size());
		}
		
		public void elementAdded(int index) {
			fireIntervalAdded(this, index, index);
		}

		public void elementRemoved(int index) {
			fireIntervalRemoved(this, index, index);
		}

		public void elementReplaced(int index) {
			fireContentsChanged(this, index, index);
		}
	
	}
	
	public Object getElementAt(int index) {
		return _list.get(index);
	}

	public int getSize() {
		return _list.size();
	}
	
	private static final long serialVersionUID = 1L;

}
