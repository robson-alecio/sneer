package sneer.pulp.reactive.listsorter.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import sneer.pulp.reactive.listsorter.ListSorter.SignalChooser;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.ListSignalOwnerReference;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.VisitorAdapter;
import wheel.reactive.lists.impl.ListRegisterImpl;

final class SortedList<T> extends VisitorAdapter<T>{
	
	private final ListSignal<T> _input;
	private final Comparator<T> _comparator;
	private final ListRegisterImpl<T> _sorted = new ListRegisterImpl<T>();
	private final Map<T, Omnivore<Object>> _elementReceivers = new HashMap<T, Omnivore<Object>>();
	private Omnivore<ListValueChange<T>> _receiverAvoidGc;
	private SignalChooser<T> _chooser;
	
	SortedList(ListSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser) {
		_input = input;
		_comparator = comparator;
		_chooser = chooser;
		synchronized (_input) {
			init();
			initReceiver();
		}
	}
	
	private void init() {
		for (T element : _input) {
			sortedAdd(element);
		}		
	}
	
	private void initReceiver() {
		_receiverAvoidGc = new Omnivore<ListValueChange<T>>(){@Override public void consume(ListValueChange<T> change) {
			change.accept(SortedList.this);
		}};
		_input.addListReceiver(_receiverAvoidGc);
	}
	
	private void removeReceiverFromElement(T element) {
		synchronized (_elementReceivers) {
			if (_chooser == null) return;
			if (!_elementReceivers.containsKey(element) ) return;
			
			_elementReceivers.remove(element);
		}
	}

	private void addReceiverToElement(T element) {
		synchronized (_elementReceivers) {
			if (_chooser == null) return;
			
			Omnivore<Object> receiver = createElementReceiver(element);
			_elementReceivers.put(element, receiver);
			
			for (Signal<?> signal : _chooser.signalsToReceiveFrom(element))
				signal.addReceiver(receiver);
		}
	}

	private Omnivore<Object> createElementReceiver(final T element) {
		return new Omnivore<Object>() { 
			boolean isFirstTime = true;
			public void consume(Object ignored1) {
				if(isFirstTime){
					isFirstTime = false;
					return;
				}
				move(element);
			}
		};
	}

	private int findSortedLocation(T element) {
		T[] array = _sorted.output().toArray();
		return findSortedLocation(element, array);
	}
	
	private int findSortedLocation(T element, T[] array) {
		int location = Arrays.binarySearch(array, element, _comparator);
		location = location<0 ? -location-1 : location;
		
		if(isInvalidLocation(array, location)){//Optimize try don't repeat binary search
			array[location] = array[location+1];
			location = findSortedLocation(element, array);
		}
		
		return location;
	}

	private boolean isInvalidLocation(T[] array, int location) {
		if(array.length>location+1){
			return _comparator.compare(array[location], array[location+1]) > 0;
		}
		return false;
	}
	
	private void sortedAdd(T element) {
		synchronized (_sorted) {
			int location = findSortedLocation(element);
			_sorted.addAt(location, element);
		}
		addReceiverToElement(element);
	}
	
	private void remove(T element) {
		synchronized (_sorted) {
			_sorted.remove(element);
		}
		removeReceiverFromElement(element);
	}
	
	private void replace(T oldElement, T newElement) {
		remove(oldElement);
		sortedAdd(newElement);
	}

	private void move(T element) {
		synchronized (_sorted) {
			int oldIndex = _sorted.indexOf(element);
			int newIndex = findSortedLocation(element);
			
			_sorted.move(oldIndex, newIndex);
		}
	}

	public ListSignal<T> output() {
		return new ListSignalOwnerReference<T>(_sorted.output(), this);
	}
	
	@Override public void elementAdded(int index, T element) { sortedAdd(element); }
	@Override public void elementInserted(int index, T element) { sortedAdd(element); }
	@Override public void elementRemoved(int index, T element) { remove(element); }
	@Override public void elementReplaced(int index, T oldElement, T newElement) { replace(oldElement, newElement); }
	
	@Override
	public String toString() {
		return "SortedList - size: " +  _sorted.output().size().currentValue() + 
				" (" + Arrays.toString(_sorted.output().toArray()) + ")";
	}
}