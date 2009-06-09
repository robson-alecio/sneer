package sneer.bricks.pulp.reactive.collections.listsorter.impl;

import java.util.Comparator;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.collections.CollectionSignal;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.collections.listsorter.ListSorter;
import sneer.bricks.pulp.reactive.signalchooser.SignalChooser;

class ListSorterImpl implements ListSorter{

	private static final Signal<?>[] EMPTY = new Signal<?>[0];
	
	@Override
	public <T> ListSignal<T> sort(final CollectionSignal<T> input, final Comparator<T> comparator, final SignalChooser<T> chooser) {
		return new ReactiveSorter<T>(input, comparator, chooser).output();
	}
	
	@Override
	public <T> ListSignal<T> sort(final CollectionSignal<T> input, final Comparator<T> comparator) {
		return new ReactiveSorter<T>(input, comparator, new SignalChooser<T>(){ @Override public Signal<?>[] signalsToReceiveFrom(T element) {
			return EMPTY;
		}}).output();
	}
}