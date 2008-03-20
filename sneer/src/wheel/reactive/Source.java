package wheel.reactive;

import java.io.Serializable;

import wheel.lang.Omnivore;

public interface Source<T> extends Serializable{

	Signal<T> output();
	
	Omnivore<T> setter();
	
	@Deprecated
	boolean isSameValue(T value);

}
