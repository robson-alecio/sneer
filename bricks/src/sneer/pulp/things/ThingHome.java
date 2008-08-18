package sneer.pulp.things;

import wheel.reactive.sets.SetSignal;

public interface ThingHome {

	Thing create(String name, String description);
	SetSignal<Thing> search(String tags);

}
