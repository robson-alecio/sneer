package sneer.bricks.pulp.reactive.collections.listsorter;

import java.util.Comparator;

import sneer.bricks.pulp.reactive.collections.CollectionSignal;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.signalchooser.SignalChooser;
import sneer.foundation.brickness.Brick;

@Brick
public interface ListSorter {

	<T> ListSignal<T> sort(CollectionSignal<T> input, Comparator<T> comparator);
	<T> ListSignal<T> sort(CollectionSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser);

}