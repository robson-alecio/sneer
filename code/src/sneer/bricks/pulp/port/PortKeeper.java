package sneer.bricks.pulp.port;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.PickyConsumer;

@Brick
public interface PortKeeper {

	Signal<Integer> port();

	PickyConsumer<Integer> portSetter();

}
