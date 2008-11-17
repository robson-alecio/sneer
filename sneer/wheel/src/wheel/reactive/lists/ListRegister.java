package wheel.reactive.lists;

import wheel.lang.Consumer;
import wheel.reactive.CollectionRegisterBase;

public interface ListRegister<T> extends CollectionRegisterBase {

	Consumer<T> adder();
	ListSignal<T> output();

	void add(T element);
	void addAt(int index, T element);

	void remove(T element);
	void removeAt(int index);
	
	void replace(int index, T newElement);
	void move(int oldIndex, int newIndex);
}