package sneer.pulp.reactive.collections.listsorter;

import java.util.Comparator;

import sneer.brickness.Brick;
import sneer.pulp.reactive.collections.CollectionSignal;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.signalchooser.SignalChooser;

@Brick
public interface ListSorter {

	<T> ListSignal<T> sort(CollectionSignal<T> input, Comparator<T> comparator);
	<T> ListSignal<T> sort(CollectionSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser);

}