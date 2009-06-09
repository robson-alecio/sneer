package spikes.sneer.pulp.things;

import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.brickness.Brick;

@Brick
public interface ThingHome {

	Thing create(String name, String description);

	SetSignal<Thing> search(String tags);

}
