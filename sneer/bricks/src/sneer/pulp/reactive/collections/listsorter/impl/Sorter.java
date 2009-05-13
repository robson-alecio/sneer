package sneer.pulp.reactive.collections.listsorter.impl;

import static sneer.commons.environments.Environments.my;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.pulp.reactive.collections.CollectionChange;
import sneer.pulp.reactive.collections.CollectionSignal;
import sneer.pulp.reactive.collections.CollectionSignals;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.pulp.reactive.signalchooser.SignalChoosers;

final class Sorter<T> implements ListOfSignalsReceiver<T>{

	private final CollectionSignal<T> _input;

	private final SignalChoosers _signalChoosers = my(SignalChoosers.class);
	
	@SuppressWarnings("unused")
	private Object _refToAvoidGc;
	private final SignalChooser<T> _chooser;	
	private final Comparator<T> _comparator;
	private final ListRegister<T> _sorted;
	private final Consumer<CollectionChange<T>> _receiverAvoidGc;
	
	private final Object _monitor = new Object();
	
	Sorter(CollectionSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser) {
		_input = input;
		_chooser = chooser;
		_comparator = comparator;
		_sorted = my(CollectionSignals.class).newListRegister();
		_receiverAvoidGc = new Consumer<CollectionChange<T>>(){@Override public void consume(CollectionChange<T> change) {
			for (T element : change.elementsRemoved()) 
				elementRemoved(element);
			
			for (T element : change.elementsAdded()) 
				elementAdded(element);
		}};
		
		synchronized (_monitor) {
			for (T element : _input)  
				sortedAdd(element);
			_input.addReceiver(_receiverAvoidGc);
		}
		
		_refToAvoidGc = _signalChoosers.receive(input, this);
	}
	
	ListSignal<T> output() {
		return my(WeakReferenceKeeper.class).keep(_sorted.output(), this);
	}
	
	private void elementAdded(T element) { 
		synchronized (_monitor) {
			int location = findSortedLocation(element);
			_sorted.addAt(location, element);
		} 
	}
	
	private void elementRemoved(T element) { 
		synchronized (_monitor) {
			_sorted.remove(element);
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
		int location = findSortedLocation(element);
		_sorted.addAt(location, element);
	}
	
	private void move(T element) {
		synchronized (_monitor) {
			int oldIndex = _sorted.output().currentIndexOf(element);
			int newIndex = findSortedLocation(element);
			
			_sorted.move(oldIndex, newIndex);
		}
	}

	@Override
	public void elementSignalChanged(T element) {
		move(element);
	}

	@Override
	public SignalChooser<T> signalChooser() {
		return _chooser;
	}
}