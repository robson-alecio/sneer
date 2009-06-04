package spikes.sneer.pulp.things;

import sneer.brickness.Brick;
import sneer.pulp.reactive.collections.SetSignal;

@Brick
public interface ThingHome {

	Thing create(String name, String description);

	SetSignal<Thing> search(String tags);

}
