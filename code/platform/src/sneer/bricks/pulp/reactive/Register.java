package sneer.bricks.pulp.reactive;

import sneer.foundation.lang.Consumer;

public interface Register<T>{

	Signal<T> output();
	
	Consumer<T> setter();

}
