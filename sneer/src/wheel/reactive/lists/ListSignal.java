package wheel.reactive.lists;

import wheel.lang.Omnivore;

public interface ListSignal<VO> extends Iterable<VO> {
	
	public void addListReceiver(Omnivore<ListValueChange> receiver);
	public void removeListReceiver(Omnivore<ListValueChange> receiver);

	public VO currentGet(int index);
	public int currentSize();

}
