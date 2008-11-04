package wheel.reactive;

import wheel.lang.Omnivore;

public interface Register<T>{

	Signal<T> output();
	
	Omnivore<T> setter();

}
