package sneer.bricks.pulp.reactive;

import sneer.bricks.hardware.cpu.lang.Consumer;

public interface Register<T>{

	Signal<T> output();
	
	Consumer<T> setter();

}
