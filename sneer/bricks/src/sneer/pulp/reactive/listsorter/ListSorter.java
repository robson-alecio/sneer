package sneer.pulp.reactive.listsorter;

import java.util.Comparator;

import sneer.brickness.Brick;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import wheel.reactive.lists.ListSignal;

@Brick
public interface ListSorter {

	<T> ListSignal<T> sort(ListSignal<T> input, Comparator<T> comparator);
	<T> ListSignal<T> sort(ListSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser);

}