package sneer.pulp.reactive.listsorter;

import java.util.Comparator;

import wheel.reactive.lists.ListSignal;

public interface ListSorter {

	<T> ListSignal<T> sort(ListSignal<T> input, Comparator<T> comparator);

}