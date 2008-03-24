package wheel.reactive;

import java.io.Serializable;

import wheel.lang.Omnivore;

public interface Register<T> extends Serializable{

	Signal<T> output();
	
	Omnivore<T> setter();

}
