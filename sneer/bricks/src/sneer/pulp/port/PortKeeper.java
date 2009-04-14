package sneer.pulp.port;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;
import sneer.software.lang.PickyConsumer;

@Brick
public interface PortKeeper {

	Signal<Integer> port();

	PickyConsumer<Integer> portSetter();

}
