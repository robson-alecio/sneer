package sneer.pulp.reactive.collections.listfilter.impl;

import sneer.hardware.cpu.lang.Predicate;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ReactivePredicate;
import sneer.pulp.reactive.collections.listfilter.ListFilter;
import sneer.pulp.reactive.signalchooser.SignalChooser;

class ListFilterImpl implements ListFilter{

	private static final Signal<?>[] EMPTY = new Signal<?>[0];
	
	@Override
	public <T> ListSignal<T> filter(final ListSignal<T> input, final Predicate<T> predicate, final SignalChooser<T> chooser) {
		return new FilteredVisitor<T>(input, predicate, chooser).output();
	}
	
	@Override
	public <T> ListSignal<T> filter(final ListSignal<T> input, final Predicate<T> predicate) {
		return new FilteredVisitor<T>(input, predicate, new SignalChooser<T>(){ @Override public Signal<?>[] signalsToReceiveFrom(T element) {
			return EMPTY;
		}}).output();
	}

	@Override
	public <T> ListSignal<T> filter(ListSignal<T> input, ReactivePredicate<T> predicate) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}
}