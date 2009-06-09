package spikes.sandro.summit.register;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface SimpleRegister {

	Signal<String> output();

	Consumer<String> setter();

}
