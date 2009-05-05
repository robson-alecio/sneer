package sneer.pulp.reactive.collections.listfilter.impl;

import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.listfilter.Filter;
import sneer.pulp.reactive.collections.listfilter.ListFilter;
import sneer.pulp.reactive.signalchooser.SignalChooser;

class ListFilterImpl implements ListFilter{

	private static final Signal<?>[] EMPTY = new Signal<?>[0];
	
	@Override
	public <T> ListSignal<T> filter(final ListSignal<T> input, final Filter<T> filter, final SignalChooser<T> chooser) {
		return new FilteredVisitor<T>(input, filter, chooser).output();
	}
	
	@Override
	public <T> ListSignal<T> filter(final ListSignal<T> input, final Filter<T> filter) {
		return new FilteredVisitor<T>(input, filter, new SignalChooser<T>(){ @Override public Signal<?>[] signalsToReceiveFrom(T element) {
			return EMPTY;
		}}).output();
	}
}