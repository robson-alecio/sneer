package sneer.pulp.things;

import sneer.brickness.Brick;
import wheel.reactive.sets.SetSignal;

public interface ThingHome extends Brick {

	Thing create(String name, String description);
	SetSignal<Thing> search(String tags);

}
