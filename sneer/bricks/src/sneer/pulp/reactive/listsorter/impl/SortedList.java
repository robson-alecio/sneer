/**
 * 
 */
package sneer.pulp.reactive.listsorter.impl;

import java.util.Arrays;
import java.util.Comparator;

import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;
import wheel.reactive.lists.impl.ListRegisterImpl;

final class SortedList<T> implements Visitor<T>{
	
	private final ListSignal<T> _input;
	private final Comparator<T> _comparator;
	private final ListRegisterImpl<T> _sorted = new ListRegisterImpl<T>();
	private Omnivore<ListValueChange<T>> _receiverAvoidGc;
	
	SortedList(ListSignal<T> input, Comparator<T> comparator) {
		_input = input;
		_comparator = comparator;
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
		int location = Arrays.binarySearch(_sorted.output().toArray(), element, _comparator);
		location = location<0 ? -location-1 : location;
		_sorted.addAt(location, element);
	}

	@Override public void elementAdded(int index, T element) { sortedAdd(element); }
	@Override public void elementInserted(int index, T element) { sortedAdd(element); }
	@Override public void elementRemoved(int index, T element) { _sorted.remove(element); }
	@Override public void elementToBeRemoved(int index, T element) { /*ignore*/ }
	@Override public void elementToBeReplaced(int index, T oldElement, T newElement) { /*ignore*/ }

	@Override
	public void elementReplaced(int index, T oldElement, T newElement) {
		_sorted.remove(oldElement);
		sortedAdd(newElement);
	}

	public ListSignal<T> output() {
		return _sorted.output();
	}
}