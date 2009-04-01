package sneer.pulp.things;

import sneer.brickness.OldBrick;
import wheel.reactive.sets.SetSignal;

public interface ThingHome extends OldBrick {

	Thing create(String name, String description);
	SetSignal<Thing> search(String tags);

}
