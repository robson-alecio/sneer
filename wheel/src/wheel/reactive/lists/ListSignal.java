package wheel.reactive.lists;

import wheel.lang.Omnivore;
import wheel.reactive.CollectionSignal;

public interface ListSignal<T> extends CollectionSignal<T> {
	
	@Deprecated
	public void addListReceiver(Omnivore<ListValueChange> receiver);
	@Deprecated
	public void removeListReceiver(Object receiver);

	public T currentGet(int index);

}
