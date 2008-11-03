package sneer.pulp.reactive.listsorter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import sneer.pulp.reactive.listsorter.ListSorter;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.AbstractSignal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;
import wheel.reactive.lists.impl.ListRegisterImpl;

class ListSorterImpl<T> implements ListSorter<T>{

	@Override
	public ListSignal<T> sort(final ListSignal<T> input, final Comparator<T> comparator) {
		return new SortedListSignal(input, comparator);
	}
	
	private final class SortedListSignal extends AbstractSignal<T>{
		
		private final ListSignal<T> _input;
		private final Comparator<T> _comparator;
		private final ListRegisterImpl<T> _register = new ListRegisterImpl<T>();
		private final Object _monitor = new Object();
		
		public SortedListSignal(ListSignal<T> input, Comparator<T> comparator) {
			_input = input;
			_comparator = comparator;
			resortAll();
			addResortReceiver();
		}
		
		private void resortAll() {
			for (T element : sortedCopy())
				_register.add(element);
		}

		private List<T> sortedCopy() {
			final List<T> result = new ArrayList<T>(_input.currentSize());
			for (T element : _input)
				result.add(element);
			Collections.sort(result, _comparator);
			return result;
		}
		
		private void addResortReceiver() {
			synchronized (_monitor) {
				_input.addListReceiver(new Omnivore<ListValueChange<T>>(){@Override public void consume(ListValueChange<T> change) {
					change.accept(new Visitor<T>() {

						@Override
						public void elementInserted(int index, T value) {
							throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
						}

						@Override
						public void elementAdded(int index, T value) {
							throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
						}

						@Override
						public void elementRemoved(int index, T value) {
							throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
						}

						@Override
						public void elementReplaced(int index, T oldValue, T newValue ) {
							throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
						}

						@Override
						public void elementToBeRemoved(int index, T value) {
							throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
						}

						@Override
						public void elementToBeReplaced(int index, T oldValue, T newValue ) {
							throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
						}
						
					});
				}});
			}
		}
		
		@Override
		public T currentGet(int index) {
			return _register.output().currentGet(index);
		}
		
		@Override
		public Iterator<T> iterator() {		
			return _register.output().iterator();
		}
		
		@Override 
		public int currentSize() { 
			return _register.output().currentSize();
		}
		
		@Override 
		public Signal<Integer> size() { 
			return _register.output().size();
		}

		@Override
		public T currentValue() {
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
		}
	
		@Override 
		public void addListReceiver(Omnivore<ListValueChange<T>> receiver) {
			_register.output().addListReceiver(receiver);
		}
		
		@Override 
		public void removeListReceiver(Object receiver) { 
			_register.output().removeListReceiver(receiver);
		}
	}
}