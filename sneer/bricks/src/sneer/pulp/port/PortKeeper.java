package sneer.pulp.port;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;
import wheel.lang.PickyConsumer;

@Brick
public interface PortKeeper {

	Signal<Integer> port();

	PickyConsumer<Integer> portSetter();

}
