package wheel.reactive.lists;

import wheel.lang.Omnivore;
import wheel.reactive.CollectionSignal;

public interface ListSignal<T> extends CollectionSignal<T> {
	
	public void addListReceiver(Omnivore<ListValueChange<T>> receiver);
	public void removeListReceiver(Object receiver);

	public T currentGet(int index);

}
