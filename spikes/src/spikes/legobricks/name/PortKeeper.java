package spikes.legobricks.name;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface PortKeeper {

	Signal<Integer> port();

	Omnivore<Integer> portSetter();

}
