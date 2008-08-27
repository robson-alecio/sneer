package sneer.skin.widgets.reactive.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import sneer.skin.widgets.reactive.ListModel;
import wheel.lang.Collections;
import wheel.reactive.lists.ListRegister;

public class DefaultListModelImpl<ELEMENT> implements ListModel<ELEMENT>{

	
	private final Set<ListDataListener> _dataListeners = new HashSet<ListDataListener>();
	private final ListRegister<ELEMENT> _register;

	DefaultListModelImpl(ListRegister<ELEMENT> register) {
		_register = register;
	}

	@Override
	public Iterator<ELEMENT> elements() {
		return _register.output().iterator();
	}

	@Override
	public int indexOf(ELEMENT element) {
		Iterator<ELEMENT> iterator = elements();
		for (int i = 0; iterator.hasNext(); i++) {
			if(element==iterator.next()) return i;
		}
		throw new IllegalArgumentException("Element not Found! " + element);
	}

	@Override
	public ELEMENT getElementAt(int index) {
		Iterator<ELEMENT> iterator = elements();
		for (int i = 0; iterator.hasNext(); i++) {
			ELEMENT tmp = iterator.next();
			if(i==index) return tmp;
		}
		throw new ArrayIndexOutOfBoundsException(index);
	}

	@Override
	public int getSize() {
		return Collections.toList(_register.output()).size(); //Optimize
	}	
	
	@Override
	public void addElement(ELEMENT element) {
		_register.add(element);
		informChange(this, ListDataEvent.INTERVAL_ADDED, 0, this.getSize()); //Fix the range indexes
	}
	@Override
	public void addElementAt(ELEMENT element, int index) {
		_register.add(element); //Implement registe.addAt
		informChange(this, ListDataEvent.INTERVAL_ADDED, index, index); //Fix the range indexes
	}

	@Override
	public void removeElement(ELEMENT element) {
		_register.remove(element);
		informChange(this, ListDataEvent.INTERVAL_REMOVED, 0, this.getSize()); //Fix the range indexes
	}
	@Override
	public void removeElementAt(int index) {
		_register.remove(index);
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