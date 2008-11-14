package wheel.reactive.lists;

import wheel.lang.Consumer;
import wheel.reactive.CollectionSignal;

public interface ListSignal<T> extends CollectionSignal<T> {
	
	public void addListReceiver(Consumer<ListValueChange<T>> receiver);
	public void removeListReceiver(Object receiver);

	public T currentGet(int index);
	
	public T[] toArray();

}
