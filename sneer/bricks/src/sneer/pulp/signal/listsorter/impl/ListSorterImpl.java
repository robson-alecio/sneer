package sneer.pulp.signal.listsorter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import sneer.pulp.signal.listsorter.ListSorter;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;

class ListSorterImpl<T> implements ListSorter<T>{

	@Override
	public ListSignal<T> sort(final ListSignal<T> input, final Comparator<T> comparator) {
		
		return new ListSignal<T>(){
			private ListSignal<T> _input = input;
			private Comparator<T> _comparator = comparator;
			private List<T> sortedList = new ArrayList<T>();
			
			{addListReceiver(new Omnivore<ListValueChange>(){@Override public void consume(ListValueChange value) {
				resortAll();
			}});}
			private void resortAll() {// Optimize write a smart sort
				List<T> tmp = new ArrayList<T>(_input.currentSize());
				Iterator<T> iterator = _input.iterator();
				while (iterator.hasNext()) {
					tmp.add(iterator.next());
				}
				Collections.sort(tmp, _comparator);
				sortedList = tmp;
			}
			
			@Override
			public T currentGet(int index) {
				return sortedList.get(index);
			}
			
			@Override
			public Iterator<T> iterator() {
				return sortedList.iterator();
			}
			
			@Override public void addListReceiver(Omnivore<ListValueChange> receiver) { _input.addListReceiver(receiver);	}
			@Override public void removeListReceiver(Object receiver) { _input.removeListReceiver(receiver);	}
			@Override public int currentSize() { return _input.currentSize(); }
			@Override public Signal<Integer> size() { return _input.size(); }
		};
	}
}