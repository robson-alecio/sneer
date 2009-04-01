package sneer.pulp.reactive.listsorter;

import java.util.Comparator;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import wheel.reactive.lists.ListSignal;

public interface ListSorter extends OldBrick{

	<T> ListSignal<T> sort(ListSignal<T> input, Comparator<T> comparator);
	<T> ListSignal<T> sort(ListSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser);

}