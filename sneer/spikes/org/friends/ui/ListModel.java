package org.friends.ui;

import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;

import wheel.reactive.Receiver;
import wheel.reactive.ListSignal.ListValueChange;
import wheel.reactive.ListSignal.ListValueChangeVisitor;

public class ListModel<VO> extends AbstractListModel implements Receiver<ListValueChange<VO>> {

	private List<VO> _list;
	private ListModelValueChangeVisitor _listModelVisitor;
	
	@SuppressWarnings("unchecked")
	public ListModel(){
		_list = Collections.EMPTY_LIST;
	}

	@Override
	public void receive(ListValueChange<VO> valueChange) {
		valueChange.accept(getListModelValueChangeVisitor());
	}

	private ListModelValueChangeVisitor getListModelValueChangeVisitor() {
		if (_listModelVisitor == null)
			_listModelVisitor = new ListModelValueChangeVisitor();
		return _listModelVisitor;
	}

	private class ListModelValueChangeVisitor implements ListValueChangeVisitor<VO>{

		public void listReplaced(List<VO> newList) {
			fireIntervalRemoved(this, 0, _list.size());
			_list = newList;
			fireContentsChanged(this, 0, newList.size());
		}
		
		public void elementAdded(int index) {
			fireIntervalAdded(this, index, index);
		}

		public void elementRemoved(int index, VO oldElement) {
			fireIntervalRemoved(this, index, index);
		}

		public void elementReplaced(int index, VO oldElement) {
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
