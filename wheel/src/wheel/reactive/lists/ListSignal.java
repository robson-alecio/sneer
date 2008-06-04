package wheel.reactive.lists;

import wheel.lang.Omnivore;
import wheel.reactive.CollectionSignal;

public interface ListSignal<T> extends CollectionSignal<T> {
	
	public void addListReceiver(Omnivore<ListValueChange> receiver);
	public void removeListReceiver(Omnivore<ListValueChange> receiver);

	public T currentGet(int index);

}
