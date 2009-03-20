package sneer.pulp.reactive;

import wheel.lang.Consumer;

public interface Register<T>{

	Signal<T> output();
	
	Consumer<T> setter();

}
