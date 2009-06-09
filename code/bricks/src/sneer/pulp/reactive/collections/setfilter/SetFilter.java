package sneer.pulp.reactive.collections.setfilter;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Predicate;
import sneer.pulp.reactive.ReactivePredicate;
import sneer.pulp.reactive.collections.SetSignal;

@Brick
public interface SetFilter {

	<T> SetSignal<T> filter(SetSignal<T> input, Predicate<T> predicate);
	<T> SetSignal<T> filter(SetSignal<T> input, ReactivePredicate<T> predicate);
}