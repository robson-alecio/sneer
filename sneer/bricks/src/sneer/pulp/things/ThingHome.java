package sneer.pulp.things;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.collections.SetSignal;

public interface ThingHome extends OldBrick {

	Thing create(String name, String description);
	SetSignal<Thing> search(String tags);

}
