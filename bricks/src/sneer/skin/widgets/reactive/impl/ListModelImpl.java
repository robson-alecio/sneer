package sneer.skin.widgets.reactive.impl;

import static wheel.lang.Types.cast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import sneer.skin.widgets.reactive.ListModel;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public class ListModelImpl implements ListModel{

	private final Signal<Object[]> _source;
	private final Consumer<Object[]> _setter;
	private final Set<ListDataListener> _dataListeners = new TreeSet<ListDataListener>();

	public ListModelImpl(Signal<Object[]> source, Consumer<Object[]> setter) {
		_source = source;
		_setter = setter;
	}

	@SuppressWarnings("unchecked")
	private List<Object> getSourceAsImmutableList() {
		return cast(Arrays.asList(_source.currentValue()));
	}

	@Override public Iterator<Object> elements() { return getSourceAsImmutableList().iterator();}
	@Override public int indexOf(Object element) { return getSourceAsImmutableList().indexOf(element);}
	@Override public Object getElementAt(int index) {return getSourceAsImmutableList().get(index);}
	@Override public int getSize() {return getSourceAsImmutableList().size();}	

	
	@Override
	public void addElement(Object element) {
		synchronized (_source) {
			List<Object> lst = new ArrayList<Object>(Arrays.asList(_source.currentValue()));
			lst.add(element);
			_setter.consume(lst.toArray());
		}
	}

	@Override
	public void removeElement(Object element) {
		synchronized (_source) {
			List<Object> lst = new ArrayList<Object>(Arrays.asList(_source.currentValue()));
			lst.remove(element);
			_setter.consume(lst.toArray());
		}
	}

	@Override
	public void removeElementAt(int fromIndex) {
		synchronized (_source) {
			List<Object> lst = new ArrayList<Object>(Arrays.asList(_source.currentValue()));
			lst.remove(fromIndex);
			_setter.consume(lst.toArray());
		}
	}
	
	@Override
	public void addElementAt(Object element, int index) {
		synchronized (_source) {
			Object[] values = _source.currentValue();
			Object[] retorno = new Object[values.length+1];
			for (int i = 0; i < values.length; i++) {
				if(i<index){
					retorno[i]=values[i];
					continue;
				}
				retorno[i+1]=values[i];
			}
			retorno[index] = element;
			_setter.consume(retorno);
		}
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