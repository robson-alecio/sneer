package sneer.pulp.reactive.listsorter.impl;

import java.util.Comparator;

import sneer.pulp.reactive.listsorter.ListSorter;
import wheel.reactive.lists.ListSignal;

class ListSorterImpl implements ListSorter{

	@Override
	public <T> ListSignal<T> sort(final ListSignal<T> input, final Comparator<T> comparator, final SignalChooser<T> chooser) {
		return new SortedList<T>(input, comparator, chooser).output();
	}
}