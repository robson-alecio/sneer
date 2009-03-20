package wheel.reactive.lists;

import wheel.lang.Consumer;
import wheel.reactive.CollectionRegister;

public interface ListRegister<T> extends CollectionRegister<T> {

	Consumer<T> adder();
	ListSignal<T> output();

	void add(T element);
	void addAt(int index, T element);

	void remove(T element);
	void removeAt(int index);
	
	void replace(int index, T newElement);
	void move(int oldIndex, int newIndex);
}