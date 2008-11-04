/**
 * 
 */
package sneer.pulp.reactive.listsorter.impl;

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
	private final Object _monitor = new Object();
	
	SortedList(ListSignal<T> input, Comparator<T> comparator) {
		_input = input;
		_comparator = comparator;
		init();
		addReceiver();
	}
	
	private void init() {
//		List<T> tmp = copy(_input);
//		Collections.sort(tmp, _comparator);
//		
//		for (T element : tmp)
//			_sorted.add(element);
	}

//	private Object[] copy(ListSignal<T> source) {
//		Object result[] = new Object[source.currentSize()];
//		for (int i = 0; i < result.length; i++) {
//			result[i] = source.currentGet(i);
//		}
//		return result;
//	}
	
	private void addReceiver() {
		synchronized (_monitor) {
			_input.addListReceiver(new Omnivore<ListValueChange<T>>(){@Override public void consume(ListValueChange<T> change) {
				change.accept(SortedList.this);
			}});
		}
	}

	@Override
	public void elementAdded(int index, T element) {
//		Collections.binarySearch(copy(), element, _comparator);
		
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void elementInserted(int index, T element) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void elementRemoved(int index, T element) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void elementReplaced(int index, T oldElement, T newElement) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void elementToBeRemoved(int index, T element) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void elementToBeReplaced(int index, T oldElement, T newElement) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	public ListSignal<T> output() {
		return _sorted.output();
	}
}