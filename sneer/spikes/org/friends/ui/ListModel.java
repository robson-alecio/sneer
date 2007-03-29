package org.friends.ui;

import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;

import wheel.reactive.ListSignal.Receiver;


public class ListModel<VO> extends AbstractListModel implements Receiver<VO> {

	private List<VO> _list;
	
	@SuppressWarnings("unchecked")
	public ListModel(){
		_list = Collections.EMPTY_LIST;
	}

	public void listReplaced(List<VO> newList) {
		fireIntervalRemoved(this, 0, _list.size());
		_list = newList;
		fireContentsChanged(this, 0, newList.size());
	}
	
	public void elementAdded(int index) {
		fireIntervalAdded(this, index, index);
	}

	public void elementRemoved(int index, VO oldElement) {
		throw new UnsupportedOperationException();
		//fireIntervalRemoved(this, index, index);
	}

	public void elementReplaced(int index, VO oldElement) {
		throw new UnsupportedOperationException();
		//fireContentsChanged(this, index, index);
	}

	public Object getElementAt(int index) {
		return _list.get(index);
	}

	public int getSize() {
		return _list.size();
	}

	
	private static final long serialVersionUID = 1L;



}
