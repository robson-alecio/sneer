/**
 * 
 */
package sneer.pulp.reactive.listsorter.impl;

import java.util.Arrays;
import java.util.Comparator;

import sneer.pulp.reactive.listsorter.ListSorter.SignalChooser;
import wheel.lang.Omnivore;
import wheel.reactive.impl.ListSignalOwnerReference;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.VisitorAdapter;
import wheel.reactive.lists.impl.ListRegisterImpl;

final class SortedList<T> extends VisitorAdapter<T>{
	
	private final ListSignal<T> _input;
	private final Comparator<T> _comparator;
	private final ListRegisterImpl<T> _sorted = new ListRegisterImpl<T>();
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
		T tmp[] = _input.toArray();
		Arrays.sort(tmp, _comparator);
		for (T element : tmp) 
			_sorted.add(element);
	}
	
	private void initReceiver() {
		_receiverAvoidGc = new Omnivore<ListValueChange<T>>(){@Override public void consume(ListValueChange<T> change) {
			change.accept(SortedList.this);
		}};
		_input.addListReceiver(_receiverAvoidGc);
	}
	
	private void sortedAdd(T element) {
		synchronized (_sorted) {
			int location = Arrays.binarySearch(_sorted.output().toArray(), element, _comparator);
			location = location<0 ? -location-1 : location;
			_sorted.addAt(location, element);
		}
	}
	
	private void remove(T element) {
		synchronized (_sorted) {
			_sorted.remove(element);
		}
	}
	
	private void replace(T oldElement, T newElement) {
		remove(oldElement);
		sortedAdd(newElement);
	}

	public ListSignal<T> output() {
		return new ListSignalOwnerReference<T>(_sorted.output(), this);
	}
	
	@Override public void elementAdded(int index, T element) { sortedAdd(element); }
	@Override public void elementInserted(int index, T element) { sortedAdd(element); }
	@Override public void elementRemoved(int index, T element) { remove(element); }
	@Override public void elementReplaced(int index, T oldElement, T newElement) { replace(oldElement, newElement); }
}