package sneer.bricks.snapps.wind;

import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.software.bricks.snappstarter.Snapp;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Snapp
@Brick
public interface Wind {

	ListSignal<Shout> shoutsHeard();

	Consumer<String> megaphone();

}
