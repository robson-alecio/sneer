package sneer.pulp.reactive.collections.listfilter;

import sneer.brickness.Brick;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.signalchooser.SignalChooser;

@Brick
public interface ListFilter {

	<T> ListSignal<T> sort(ListSignal<T> input, Filter<T> filter);
	<T> ListSignal<T> sort(ListSignal<T> input, Filter<T> filter, SignalChooser<T> chooser);

}