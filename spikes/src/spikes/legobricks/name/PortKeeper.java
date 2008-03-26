package spikes.legobricks.name;

import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface PortKeeper {

	Signal<Integer> port();

	Consumer<Integer> portSetter();

}
