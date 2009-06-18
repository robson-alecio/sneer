package sneer.bricks.pulp.reactive.collections.setfilter;

import sneer.bricks.pulp.reactive.ReactivePredicate;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Predicate;

@Brick
public interface SetFilter {

	<T> SetSignal<T> filter(SetSignal<T> input, Predicate<T> predicate);
	<T> SetSignal<T> filter(SetSignal<T> input, ReactivePredicate<T> predicate);
}