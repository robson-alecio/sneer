package sneerapps.giventake.tests;

import sneer.bricks.things.Thing;
import sneerapps.giventake.GiveNTake;
import wheel.reactive.sets.SetSignal;

public interface GiveNTakeUser {

	void advertise(String title, String description);

	SetSignal<Thing> search(String tags);

	void connectTo(GiveNTakeUser peer);

	void addCounterpart(GiveNTake _gnt);

}