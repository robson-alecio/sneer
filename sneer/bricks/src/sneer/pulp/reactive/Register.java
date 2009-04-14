package sneer.pulp.reactive;

import sneer.software.lang.Consumer;

public interface Register<T>{

	Signal<T> output();
	
	Consumer<T> setter();

}
