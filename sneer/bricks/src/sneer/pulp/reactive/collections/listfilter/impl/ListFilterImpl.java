package sneer.pulp.reactive.collections.listfilter.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Predicate;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.ReactivePredicate;
import sneer.pulp.reactive.collections.SetSignal;
import sneer.pulp.reactive.collections.listfilter.ListFilter;

class ListFilterImpl implements ListFilter{

	@Override
	public <T> SetSignal<T> filter(final SetSignal<T> input, final Predicate<T> predicate) {
		ReactivePredicate<T> reactivePredicate = new ReactivePredicate<T>(){ @Override public Signal<Boolean> evaluate(T value) {
			return my(Signals.class).constant(predicate.evaluate(value));
		}};
		return new FilteredVisitor<T>(input, reactivePredicate).output();	
	}

	@Override
	public <T> SetSignal<T> filter(SetSignal<T> input, ReactivePredicate<T> predicate) {
		return new FilteredVisitor<T>(input, predicate).output();	
	}
}