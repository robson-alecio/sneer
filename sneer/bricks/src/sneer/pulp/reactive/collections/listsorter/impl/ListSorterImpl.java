package sneer.pulp.reactive.collections.listsorter.impl;

import java.util.Comparator;

import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.collections.CollectionSignal;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;

class ListSorterImpl implements ListSorter{

	private static final Signal<?>[] EMPTY = new Signal<?>[0];
	
	@Override
	public <T> ListSignal<T> sort(final CollectionSignal<T> input, final Comparator<T> comparator, final SignalChooser<T> chooser) {
		return new Sorter<T>(input, comparator, chooser).output();
	}
	
	@Override
	public <T> ListSignal<T> sort(final CollectionSignal<T> input, final Comparator<T> comparator) {
		return new Sorter<T>(input, comparator, new SignalChooser<T>(){ @Override public Signal<?>[] signalsToReceiveFrom(T element) {
			return EMPTY;
		}}).output();
	}
}