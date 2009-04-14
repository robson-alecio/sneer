package sneer.pulp.reactive.collections;

import java.util.List;

import sneer.pulp.events.EventSource;
import wheel.reactive.lists.ListChange;

public interface ListSignal<T> extends CollectionSignal<T>, EventSource<ListChange<T>> {
	
	T currentGet(int index);
	int currentIndexOf(T element);
	List<T> currentElements();
	
}
