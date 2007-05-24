package wheel.reactive.lists;

import wheel.reactive.Receiver;

public interface ListSource<VO> {

	ListSignal<VO> output();

	//Fix: ListSource should use a consumer instead of this
	void add(VO element);
	boolean remove(VO element);

}