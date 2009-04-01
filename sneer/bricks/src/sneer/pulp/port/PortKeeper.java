package sneer.pulp.port;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;
import wheel.lang.PickyConsumer;

public interface PortKeeper extends OldBrick {

	Signal<Integer> port();

	PickyConsumer<Integer> portSetter();

}
