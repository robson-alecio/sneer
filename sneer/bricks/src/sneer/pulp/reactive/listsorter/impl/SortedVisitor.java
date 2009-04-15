package sneer.pulp.reactive.listsorter.impl;

import static sneer.commons.environments.Environments.my;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sneer.pulp.reactive.collections.ListChange;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ReactiveCollections;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.pulp.reactive.signalchooser.SignalChooserManager;
import sneer.pulp.reactive.signalchooser.SignalChooserManagerFactory;
import sneer.software.lang.Consumer;
import wheel.reactive.impl.ListSignalOwnerReference;
import wheel.reactive.lists.VisitorAdapter;

final class SortedVisitor<T> extends VisitorAdapter<T> implements ListOfSignalsReceiver<T>{

	private final ListSignal<T> _input;
	private final SorterSupport _sorter;

	private final SignalChooserManagerFactory _signalChooserManagerFactory = my(SignalChooserManagerFactory.class);
	
	@SuppressWarnings("unused")
	private SignalChooserManager<T> _signalChooserManagerToAvoidGc;
	private final SignalChooser<T> _chooser;	
	
	private final Object _monitor = new Object();
	
	SortedVisitor(ListSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser) {
		_input = input;
		_chooser = chooser;
		_sorter = new SorterSupport(comparator);
		_signalChooserManagerToAvoidGc = _signalChooserManagerFactory.newManager(input, this);
	}
	
	ListSignal<T> output() {
		return new ListSignalOwnerReference<T>(_sorter._sorted.output(), this);
	}
	
	@Override public void elementAdded(int index, T element) { _sorter.sortedAdd(element); }
	@Override public void elementInserted(int index, T element) { _sorter.sortedAdd(element); }
	@Override public void elementRemoved(int index, T element) { _sorter.remove(element); }
	@Override public void elementReplaced(int index, T oldElement, T newElement) { _sorter.replace(oldElement, newElement); }
	
	private class SorterSupport{
		
		private final Comparator<T> _comparator;
		private final ListRegister<T> _sorted = my(ReactiveCollections.class).newListRegister();
		private final Consumer<ListChange<T>> _receiverAvoidGc = new Consumer<ListChange<T>>(){@Override public void consume(ListChange<T> change) {
			change.accept(SortedVisitor.this);
		}};
		
		private SorterSupport(Comparator<T> comparator) {
			_comparator = comparator;
			synchronized (_monitor) {
				initSortedList();
				_input.addReceiver(_receiverAvoidGc);
			}
		}

		private void initSortedList() {
			for (T element : _input)  
				sortedAdd(element);
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

		private void replace(T oldElement, T newElement) {
			remove(oldElement);
			sortedAdd(newElement);
		}
		
		private void move(T element) {
			synchronized (_monitor) {
				int oldIndex = _sorted.output().currentIndexOf(element);
				int newIndex = findSortedLocation(element);
				
				_sorted.move(oldIndex, newIndex);
			}
		}
	}

	@Override
	public void elementSignalChanged(int index, T element) {
		_sorter.move(element);
	}

	@Override
	public SignalChooser<T> signalChooser() {
		return _chooser;
	}
}