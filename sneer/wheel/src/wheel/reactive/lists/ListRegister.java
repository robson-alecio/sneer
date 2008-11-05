package wheel.reactive.lists;

import wheel.lang.Omnivore;
import wheel.reactive.CollectionRegisterBase;

public interface ListRegister<T> extends CollectionRegisterBase {

	Omnivore<T> adder();
	ListSignal<T> output();

	int indexOf(T element);

	void add(T element);
	void addAt(int index, T element);

	void remove(T element);
	void removeAt(int index);
	
	void replace(int index, T newElement);
}