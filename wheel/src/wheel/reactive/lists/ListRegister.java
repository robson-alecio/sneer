package wheel.reactive.lists;

import wheel.lang.Omnivore;
import wheel.reactive.RegisterBase;

public interface ListRegister<VO> extends RegisterBase {

	ListSignal<VO> output();

	void add(VO element);
	Omnivore<VO> adder();
	VO get(int index);

	boolean remove(VO element);
	void remove(int index);
	
	void replace(int index, VO newElement);
	
}