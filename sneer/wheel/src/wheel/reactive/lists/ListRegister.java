package wheel.reactive.lists;

import wheel.lang.Omnivore;
import wheel.reactive.RegisterBase;

public interface ListRegister<T> extends RegisterBase {

	ListSignal<T> output();

	void add(T element);
	Omnivore<T> adder();

	void remove(T element);
	void remove(int index);
	
	void replace(int index, T newElement);
	
}