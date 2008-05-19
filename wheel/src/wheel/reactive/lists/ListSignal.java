package wheel.reactive.lists;

import java.io.Serializable;

import wheel.lang.Omnivore;

public interface ListSignal<VO> extends Iterable<VO>, Serializable {
	
	public void addListReceiver(Omnivore<ListValueChange> receiver);
	public void removeListReceiver(Omnivore<ListValueChange> receiver);

	public int currentSize();
	public VO currentGet(int index);

}
