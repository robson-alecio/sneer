package wheel.reactive;

public interface Source<T> {

	Signal<T> output();
	
	Consumer<T> setter();
	
	boolean isSameValue(T value);

}
