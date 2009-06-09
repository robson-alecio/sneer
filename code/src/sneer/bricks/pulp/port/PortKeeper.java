package sneer.bricks.pulp.port;

import sneer.bricks.hardware.cpu.lang.PickyConsumer;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface PortKeeper {

	Signal<Integer> port();

	PickyConsumer<Integer> portSetter();

}
