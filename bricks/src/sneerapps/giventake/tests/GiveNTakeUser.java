package sneerapps.giventake.tests;

import sneer.bricks.things.Thing;
import wheel.reactive.sets.SetSignal;

public interface GiveNTakeUser {

	void advertise(String title, String description);

	SetSignal<Thing> search(String tags);

	void connectTo(GiveNTakeUser peer);

	MeMock yourMe();

}