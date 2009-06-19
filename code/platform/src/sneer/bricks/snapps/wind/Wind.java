package sneer.bricks.snapps.wind;

import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface Wind {

	ListSignal<Shout> shoutsHeard();

	Consumer<String> megaphone();

}
