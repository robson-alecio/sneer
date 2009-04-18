package sneer.pulp.reactive.collections;

import sneer.hardware.cpu.lang.Consumer;

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