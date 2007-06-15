package sneer.kernel.communication;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface Connection {

	Signal<Object> input();
	Omnivore<Object> output();

}
