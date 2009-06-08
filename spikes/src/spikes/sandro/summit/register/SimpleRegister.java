package spikes.sandro.summit.register;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;

@Brick
public interface SimpleRegister {

	Signal<String> output();

	Consumer<String> setter();

}
