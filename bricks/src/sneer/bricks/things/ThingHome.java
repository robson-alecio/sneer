package sneer.bricks.things;

import java.util.Collection;

public interface ThingHome {

	Thing create(String name, String description);
	Collection<Thing> find(String tags);

}
