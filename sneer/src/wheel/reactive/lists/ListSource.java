package wheel.reactive.lists;

import wheel.lang.Omnivore;

public interface ListSource<VO> {

	ListSignal<VO> output();

	//Fix: ListSource should use the omnivore instead of this
	void add(VO element);
	boolean remove(VO element);

	Omnivore<VO> adder();

}