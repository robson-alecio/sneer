package wheel.reactive;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;

public interface Source<T> {

	Signal<T> output();
	
	Omnivore<T> setter();
	
	boolean isSameValue(T value);

}
