package wheel.reactive.lists;

import wheel.lang.Omnivore;

public interface ListSource<VO> {

	ListSignal<VO> output();

	void add(VO element);
	Omnivore<VO> adder();

	boolean remove(VO element);
	void remove(int index);
	
	void replace(int index, VO newElement);
	
}