package sneer.bricks.pulp.reactive.collections;

import java.util.List;

import sneer.foundation.lang.Consumer;

public interface ListSignal<T> extends CollectionSignal<T> {
	
	T currentGet(int index);
	int currentIndexOf(T element);
	List<T> currentElements();
	
	void addListReceiver(Consumer<? super ListChange<T>> receiver);
	void removeListReceiver(Object receiver);
}