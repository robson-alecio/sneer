package sneer.bricks.pulp.reactive.collections.setfilter;

import sneer.bricks.hardware.cpu.lang.Predicate;
import sneer.bricks.pulp.reactive.ReactivePredicate;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.brickness.Brick;

@Brick
public interface SetFilter {

	<T> SetSignal<T> filter(SetSignal<T> input, Predicate<T> predicate);
	<T> SetSignal<T> filter(SetSignal<T> input, ReactivePredicate<T> predicate);
}