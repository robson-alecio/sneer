package wheel.reactive;

public interface CollectionSignal<T> extends Iterable<T>{

	Signal<Integer> size();
	
	//Refactor deprecate in favour of size().currentValue()
	int currentSize();

}
