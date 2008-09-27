package wheel.reactive;

import wheel.lang.Omnivore;

public interface Register<T> extends RegisterBase {

	Signal<T> output();
	
	Omnivore<T> setter();

}
