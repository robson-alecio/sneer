package sneerapps.giventake.tests;

import java.util.Collection;

import sneer.bricks.things.Thing;

public interface GiveNTakeUser {

	void advertise(String title, String description);

	Collection<Thing> search(String tags);

}