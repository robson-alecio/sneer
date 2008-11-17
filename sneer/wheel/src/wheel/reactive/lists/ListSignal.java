package wheel.reactive.lists;

import java.util.List;

import wheel.lang.Consumer;
import wheel.reactive.CollectionSignal;

public interface ListSignal<T> extends CollectionSignal<T> {
	
	void addListReceiver(Consumer<ListValueChange<T>> receiver);
	void removeListReceiver(Object receiver);

	T currentGet(int index);
	int currentIndexOf(T element);
	List<T> currentElements();
	
}
