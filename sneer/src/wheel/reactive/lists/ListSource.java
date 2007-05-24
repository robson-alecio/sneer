package wheel.reactive.lists;

import wheel.reactive.Receiver;

public interface ListSource<VO> {

	ListSignal<VO> output();

	void add(VO element);
	boolean remove(VO element);

}