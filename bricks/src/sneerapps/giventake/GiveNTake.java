package sneerapps.giventake;

import java.util.Collection;

import sneer.bricks.things.Thing;
import sneer.lego.Brick;
import wheel.reactive.sets.SetSignal;

public interface GiveNTake extends Brick {

	void advertise(Thing thing);

	Collection<Thing> localSearch(String tags);
	SetSignal<Thing> firstLevelRemoteSearch(String tags);

}
