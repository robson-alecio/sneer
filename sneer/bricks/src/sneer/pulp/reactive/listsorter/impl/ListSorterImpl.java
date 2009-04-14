package sneer.pulp.reactive.listsorter.impl;

import java.util.Comparator;

import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;

class ListSorterImpl implements ListSorter{

	private static final Signal<?>[] EMPTY = new Signal<?>[0];
	
	@Override
	public <T> ListSignal<T> sort(final ListSignal<T> input, final Comparator<T> comparator, final SignalChooser<T> chooser) {
		return new SortedVisitor<T>(input, comparator, chooser).output();
	}
	
	@Override
	public <T> ListSignal<T> sort(final ListSignal<T> input, final Comparator<T> comparator) {
		return new SortedVisitor<T>(input, comparator, new SignalChooser<T>(){ @Override public Signal<?>[] signalsToReceiveFrom(T element) {
			return EMPTY;
		}}).output();
	}
}