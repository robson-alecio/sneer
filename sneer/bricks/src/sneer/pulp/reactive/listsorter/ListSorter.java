package sneer.pulp.reactive.listsorter;

import java.util.Comparator;

import sneer.kernel.container.Brick;
import sneer.pulp.reactive.signalchooser.ElementsObserverFactory.SignalChooser;
import wheel.reactive.lists.ListSignal;

public interface ListSorter extends Brick{

	<T> ListSignal<T> sort(ListSignal<T> input, Comparator<T> comparator);
	<T> ListSignal<T> sort(ListSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser);

}