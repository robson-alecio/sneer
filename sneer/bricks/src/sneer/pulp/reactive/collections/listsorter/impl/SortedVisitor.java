package sneer.pulp.reactive.collections.listsorter.impl;

import static sneer.commons.environments.Environments.my;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.collections.ListChange;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ReactiveCollections;
import sneer.pulp.reactive.collections.ListChange.Visitor;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.pulp.reactive.signalchooser.SignalChoosers;
import wheel.reactive.impl.ListSignalOwnerReference;

final class SortedVisitor<T> implements Visitor<T>, ListOfSignalsReceiver<T>{

	private final ListSignal<T> _input;

	private final SignalChoosers _signalChooserManagerFactory = my(SignalChoosers.class);
	
	@SuppressWarnings("unused")
	private Object _refToAvoidGc;
	private final SignalChooser<T> _chooser;	
	private final Comparator<T> _comparator;
	private final ListRegister<T> _sorted;
	private final Consumer<ListChange<T>> _receiverAvoidGc;
	
	private final Object _monitor = new Object();
	
	SortedVisitor(ListSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser) {
		_input = input;
		_chooser = chooser;
		_comparator = comparator;
		_sorted = my(ReactiveCollections.class).newListRegister();
		_receiverAvoidGc = new Consumer<ListChange<T>>(){@Override public void consume(ListChange<T> change) {
			change.accept(SortedVisitor.this);
		}};
		
		synchronized (_monitor) {
			for (T element : _input)  
				sortedAdd(element);
			_input.addReceiver(_receiverAvoidGc);
		}
		
		_refToAvoidGc = _signalChooserManagerFactory.newManager(input, this);
	}
	
	ListSignal<T> output() {
		return new ListSignalOwnerReference<T>(_sorted.output(), this);
	}
	
	@Override public void elementAdded(int index, T element) { 
		synchronized (_monitor) {
			int location = findSortedLocation(element);
			_sorted.addAt(location, element);
		} 
	}
	
	@Override public void elementRemoved(int index, T element) { 
		synchronized (_monitor) {
			_sorted.remove(element);
		} 
	}
	
	@Override public void elementReplaced(int index, T oldElement, T newElement) { 
		remove(oldElement);
		sortedAdd(newElement); 
	}
	
	@Override public void elementMoved(int oldIndex, int newIndex, T element) { 
		synchronized (_monitor) {
			int oldIndex1 = _sorted.output().currentIndexOf(element);
			int newIndex1 = findSortedLocation(element);
			_sorted.move(oldIndex1, newIndex1);
		}
	}	
	
	private int findSortedLocation(T element) {
		return findSortedLocation(element, _sorted.output().currentElements());
	}
	
	private int findSortedLocation(T element, List<T> list) {
		int location = Collections.binarySearch(list, element, _comparator);
		location = location<0 ? -location-1 : location;
		
		if(isInvalidLocation(list, location)){  //Refactor: What is this for?
			list.set(location, list.get(location+1));
			location = findSortedLocation(element, list);
		}
		
		return location;
	}

	private boolean isInvalidLocation(List<T> list, int location) {
		if (location+1 >= list.size()) return false;
		
		return _comparator.compare(list.get(location), list.get(location+1)) > 0;
	}
	
	private void sortedAdd(T element) {
		synchronized (_monitor) {
			int location = findSortedLocation(element);
			_sorted.addAt(location, element);
		}
	}
	
	private void remove(T element) {
		synchronized (_monitor) {
			_sorted.remove(element);
		}
	}
	
	private void move(T element) {
		synchronized (_monitor) {
			int oldIndex = _sorted.output().currentIndexOf(element);
			int newIndex = findSortedLocation(element);
			
			_sorted.move(oldIndex, newIndex);
		}
	}

	@Override
	public void elementSignalChanged(int index, T element) {
		move(element);
	}

	@Override
	public SignalChooser<T> signalChooser() {
		return _chooser;
	}
}