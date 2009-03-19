package wheel.reactive;

import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public interface Register<T>{

	Signal<T> output();
	
	Consumer<T> setter();

}
