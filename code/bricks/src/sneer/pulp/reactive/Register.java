package sneer.pulp.reactive;

import sneer.hardware.cpu.lang.Consumer;

public interface Register<T>{

	Signal<T> output();
	
	Consumer<T> setter();

}
