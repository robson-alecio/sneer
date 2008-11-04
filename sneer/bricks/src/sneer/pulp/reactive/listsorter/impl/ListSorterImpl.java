package sneer.pulp.reactive.listsorter.impl;

import java.util.Comparator;

import sneer.pulp.reactive.listsorter.ListSorter;
import wheel.reactive.lists.ListSignal;

class ListSorterImpl<T> implements ListSorter<T>{

	@Override
	public ListSignal<T> sort(final ListSignal<T> input, final Comparator<T> comparator) {
		return new SortedList<T>(input, comparator).output();
	}
}