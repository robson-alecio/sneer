package sneer.bricks.snapps.wind;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.foundation.brickness.Brick;

@Brick
public interface Wind {

	ListSignal<Shout> shoutsHeard();

	Consumer<String> megaphone();

}
