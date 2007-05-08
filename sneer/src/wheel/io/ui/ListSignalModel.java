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

	private class MyReceiver implements Receiver<ListValueChange<Object>> {

		public void receive(ListValueChange<Object> valueChange) {
			valueChange.accept(getListModelValueChangeVisitor());
		}

	}

	private List _list;
	private ListModelValueChangeVisitor _listModelVisitor;
	
	private Receiver<ListValueChange<Object>> _receiver = new MyReceiver();
	
	@SuppressWarnings("unchecked")
	public ListSignalModel(ListSignal input){
		_list = Collections.EMPTY_LIST;
		input.addListReceiver(_receiver);
	}

	private ListModelValueChangeVisitor getListModelValueChangeVisitor() {
		if (_listModelVisitor == null)
			_listModelVisitor = new ListModelValueChangeVisitor();
		return _listModelVisitor;
	}

	private class ListModelValueChangeVisitor implements ListValueChangeVisitor<Object> {

		public void listReplaced(List newList) {
			fireIntervalRemoved(this, 0, _list.size());
			_list = newList;
			fireContentsChanged(this, 0, newList.size());
		}
		
		public void elementAdded(int index) {
			fireIntervalAdded(this, index, index);
		}

		public void elementRemoved(int index, Object oldElement) {
			fireIntervalRemoved(this, index, index);
		}

		public void elementReplaced(int index, Object oldElement) {
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
