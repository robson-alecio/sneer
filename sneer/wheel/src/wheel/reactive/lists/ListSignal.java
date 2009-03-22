package wheel.reactive.lists;

import java.util.List;

import sneer.pulp.events.EventSource;
import sneer.pulp.reactive.CollectionSignal;

public interface ListSignal<T> extends CollectionSignal<T>, EventSource<ListValueChange<T>> {
	
	T currentGet(int index);
	int currentIndexOf(T element);
	List<T> currentElements();
	
}
