package sneer.pulp.reactive.collections.listfilter;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Predicate;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ReactivePredicate;
import sneer.pulp.reactive.signalchooser.SignalChooser;

@Brick
public interface ListFilter {

	<T> ListSignal<T> filter(ListSignal<T> input, Predicate<T> predicate);
	<T> ListSignal<T> filter(ListSignal<T> input, ReactivePredicate<T> predicate);
	
	
	@Deprecated <T> ListSignal<T> filter(ListSignal<T> input, Predicate<T> filter, SignalChooser<T> chooser);

}