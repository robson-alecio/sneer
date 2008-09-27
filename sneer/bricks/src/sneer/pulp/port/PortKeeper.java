package sneer.pulp.port;

import sneer.kernel.container.Brick;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface PortKeeper extends Brick {

	Signal<Integer> port();

	Consumer<Integer> portSetter();

}
