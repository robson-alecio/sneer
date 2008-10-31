package sneer.pulp.signal.listsorter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import sneer.pulp.signal.listsorter.ListSorter;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.AbstractSignal;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.impl.ListRegisterImpl;

class ListSorterImpl<T> implements ListSorter<T>{

	@Override
	public ListSignal<T> sort(final ListSignal<T> input, final Comparator<T> comparator) {
		return new SortedListSignal(input, comparator);
	}
	
	private final class SortedListSignal extends AbstractSignal<T>{
		
		private final ListSignal<T> _input;
		private final Comparator<T> _comparator;
		
		private final ListRegister<T> _register = new ListRegisterImpl<T>();
		
		private final Object _monitor = new Object();
		
		public SortedListSignal(ListSignal<T> input, Comparator<T> comparator) {
			_input = input;
			_comparator = comparator;
			resortAll();
			addResortReceiver();
		}
		
		private void resortAll() {// Optimize write a smart sort
//			synchronized (_monitor) {
//				_sortedList = sortedCopy();
//			}
		}

		private List<T> sortedCopy() {
			List<T> tmp = new ArrayList<T>(_input.currentSize());
			for (T element : _input) {
				tmp.add(element);
			}
			Collections.sort(tmp, _comparator);
			return tmp;
		}
		
		private void addResortReceiver() {
			synchronized (_monitor) {
				_input.addListReceiver(new Omnivore<ListValueChange>(){@Override public void consume(ListValueChange value) {
					resortAll();
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
		public void addListReceiver(Omnivore<ListValueChange> receiver) {
			_register.output().addListReceiver(receiver);
		}
		
		@Override 
		public void removeListReceiver(Object receiver) { 
			_register.output().removeListReceiver(receiver);
		}
	}
}