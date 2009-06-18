package spikes.sandro.summit.register;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface SimpleRegister {

	Signal<String> output();

	Consumer<String> setter();

}
