package sneer.pulp.port;

import sneer.kernel.container.Brick;
import wheel.lang.PickyConsumer;
import wheel.reactive.Signal;

public interface PortKeeper extends Brick {

	Signal<Integer> port();

	PickyConsumer<Integer> portSetter();

}
