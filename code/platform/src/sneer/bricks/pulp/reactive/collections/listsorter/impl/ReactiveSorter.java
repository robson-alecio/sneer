package sneer.bricks.pulp.reactive.collections.listsorter.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sneer.bricks.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.CollectionSignal;
import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.bricks.pulp.reactive.signalchooser.SignalChooser;
import sneer.bricks.pulp.reactive.signalchooser.SignalChoosers;
import sneer.foundation.lang.Consumer;

final class ReactiveSorter<T> implements ListOfSignalsReceiver<T>{

	private final CollectionSignal<T> _input;

	private final SignalChoosers _signalChoosers = my(SignalChoosers.class);
	
	@SuppressWarnings("unused")
	private Object _refToAvoidGc;
	
	private final SignalChooser<T> _chooser;	
	private final Comparator<T> _comparator;
	private final ListRegister<T> _sorted;
	private final Consumer<CollectionChange<T>> _receiverAvoidGc;
	
	private final Object _monitor = new Object();
	
	ReactiveSorter(CollectionSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser) {
		_input = input;
		_chooser = chooser;
		_comparator = comparator;
		_sorted = my(CollectionSignals.class).newListRegister();
		
		_receiverAvoidGc = new Consumer<CollectionChange<T>>(){@Override public void consume(CollectionChange<T> change) {
			executeChanges(change);
		}};
		
		synchronized (_monitor) {
			ArrayList<T> tmp = resortedList();
			
			for (T element : tmp) 
				_sorted.add(element);

			_input.addReceiver(_receiverAvoidGc);
		}
		
		_refToAvoidGc = _signalChoosers.receive(input, this);
	}

	private ArrayList<T> resortedList() {
		ArrayList<T> tmp = new ArrayList<T>(_input.currentElements());
		Collections.sort(tmp, _comparator);
		return tmp;
	}
	
	ListSignal<T> output() {
		return my(WeakReferenceKeeper.class).keep(_sorted.output(), this);
	}
	
	private void executeChanges(CollectionChange<T> change) {
		synchronized (_monitor) {
			for (T element : change.elementsAdded()) {
				_sorted.add(element);
				move(element); 
			}
			for (T element : change.elementsRemoved()) {
				_sorted.remove(element);
			}
		}
	}

	private void move(T element) {
		synchronized (_monitor) {
			int oldIndex = _sorted.output().currentIndexOf(element);
			ArrayList<T> tmp = resortedList();
			int newIndex = tmp.indexOf(element);
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