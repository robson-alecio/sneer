package sneer.pulp.port;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.pulp.reactive.Signal;

@Brick
public interface PortKeeper {

	Signal<Integer> port();

	PickyConsumer<Integer> portSetter();

}
