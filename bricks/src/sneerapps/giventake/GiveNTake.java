package sneerapps.giventake;

import sneer.lego.Brick;
import sneer.pulp.things.Thing;
import wheel.reactive.maps.MapSignal;
import wheel.reactive.sets.SetSignal;

public interface GiveNTake extends Brick {

	void advertise(Thing thing);

	SetSignal<Thing> search(String tags);
	SetSignal<String> activeSearches();

	MapSignal<String, SetSignal<Thing>> localResults();

}
