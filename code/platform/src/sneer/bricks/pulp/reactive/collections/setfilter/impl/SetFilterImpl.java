package sneer.bricks.pulp.reactive.collections.setfilter.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.reactive.ReactivePredicate;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.bricks.pulp.reactive.collections.setfilter.SetFilter;
import sneer.foundation.lang.Predicate;

class SetFilterImpl implements SetFilter{

	@Override
	public <T> SetSignal<T> filter(final SetSignal<T> input, final Predicate<T> predicate) {
		ReactivePredicate<T> reactivePredicate = new ReactivePredicate<T>(){ @Override public Signal<Boolean> evaluate(T value) {
			return my(Signals.class).constant(predicate.evaluate(value));
		}};
		return new Filter<T>(input, reactivePredicate).output();	
	}

	@Override
	public <T> SetSignal<T> filter(SetSignal<T> input, ReactivePredicate<T> predicate) {
		return new Filter<T>(input, predicate).output();	
	}
}