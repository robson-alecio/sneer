package wheel.reactive.lists;

import java.io.Serializable;

import wheel.lang.Omnivore;

public interface ListSource<VO> extends Serializable{

	ListSignal<VO> output();

	void add(VO element);
	Omnivore<VO> adder();
	VO get(int index);

	boolean remove(VO element);
	void remove(int index);
	
	void replace(int index, VO newElement);
	
}