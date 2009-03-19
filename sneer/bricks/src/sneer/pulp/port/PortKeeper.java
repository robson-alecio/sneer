package sneer.pulp.port;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;
import wheel.lang.PickyConsumer;

public interface PortKeeper extends Brick {

	Signal<Integer> port();

	PickyConsumer<Integer> portSetter();

}
