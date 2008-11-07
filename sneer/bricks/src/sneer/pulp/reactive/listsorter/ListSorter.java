package sneer.pulp.reactive.listsorter;

import java.util.Comparator;

import sneer.kernel.container.Brick;

import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface ListSorter extends Brick{

	public static interface SignalChooser<E> {
		Signal<?>[] signalsToReceiveFrom(E element);
	}
	
	<T> ListSignal<T> sort(ListSignal<T> input, Comparator<T> comparator);
	<T> ListSignal<T> sort(ListSignal<T> input, Comparator<T> comparator, SignalChooser<T> chooser);

}