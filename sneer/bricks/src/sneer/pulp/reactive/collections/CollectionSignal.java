package sneer.pulp.reactive.collections;

import java.util.Collection;

import sneer.pulp.reactive.Signal;



public interface CollectionSignal<T> extends Iterable<T>{

	Signal<Integer> size();
	
	//Refactor deprecate in favour of size().currentValue()
	int currentSize();
	Collection<T> currentElements();


}
