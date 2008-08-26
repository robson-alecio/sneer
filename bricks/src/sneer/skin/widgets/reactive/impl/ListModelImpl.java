package sneer.skin.widgets.reactive.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import sneer.skin.widgets.reactive.ListModel;
import sneer.skin.widgets.reactive.ListModelSetter;
import wheel.reactive.lists.ListSignal;

public class ListModelImpl<ELEMENT> implements ListModel<ELEMENT>{

	private final ListSignal<ELEMENT> _source;
	private final ListModelSetter<ELEMENT> _setter;
	
	private final Set<ListDataListener> _dataListeners = new HashSet<ListDataListener>();

	public ListModelImpl(ListSignal<ELEMENT> source, ListModelSetter<ELEMENT> setter) {
		_source = source;
		_setter = setter;
	}

	private List<ELEMENT> getSourceAsImmutableList() {
		List<ELEMENT> lst = new ArrayList<ELEMENT>();
		Iterator<ELEMENT> iterator = _source.iterator();
		while (iterator.hasNext()) {
			ELEMENT object = iterator.next();
			lst.add(object);
		}
		return lst;
	}

	@Override
	public Iterator<ELEMENT> elements() {
		return getSourceAsImmutableList().iterator();
	}

	@Override
	public int indexOf(ELEMENT element) {
		return getSourceAsImmutableList().indexOf(element);
	}

	@Override
	public ELEMENT getElementAt(int index) {
		return getSourceAsImmutableList().get(index);
	}

	@Override
	public int getSize() {
		return getSourceAsImmutableList().size();
	}	
	
	@Override
	public void addElement(ELEMENT element) {
		_setter.addElement(element);
		informChange(this, ListDataEvent.INTERVAL_ADDED, 0, this.getSize()); //Fix the range indexes
	}
	@Override
	public void addElementAt(ELEMENT element, int index) {
		_setter.addElementAt(element,index);
		informChange(this, ListDataEvent.INTERVAL_ADDED, index, index); //Fix the range indexes
	}

	@Override
	public void removeElement(ELEMENT element) {
		_setter.addElement(element);
		informChange(this, ListDataEvent.INTERVAL_REMOVED, 0, this.getSize()); //Fix the range indexes
	}
	@Override
	public void removeElementAt(int index) {
		_setter.removeElementAt(index);
		informChange(this, ListDataEvent.INTERVAL_REMOVED, index, index);
	}
	
	@Override
	public void addListDataListener(ListDataListener listener) {
		_dataListeners.add(listener);
	}

	@Override
	public void removeListDataListener(ListDataListener listener) {
		_dataListeners.remove(listener);
	}

	public void informChange(Object src, int type, int index0, int index1 ){
		ListDataEvent event = new ListDataEvent(src, type, index0, index1);
		for (ListDataListener listener : _dataListeners) {
			if(type==ListDataEvent.INTERVAL_ADDED){
				listener.intervalAdded(event);
				continue;
			}
			if(type==ListDataEvent.INTERVAL_REMOVED){
				listener.intervalRemoved(event);
				continue;
			}
		}
	}
};