package sneerapps.giventake;

import sneer.bricks.things.Thing;
import sneer.lego.Brick;
import wheel.reactive.sets.SetSignal;

public interface GiveNTake extends Brick {

	void advertise(Thing thing);

	SetSignal<Thing> search(String tags);

}