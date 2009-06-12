package sneer.bricks.snapps.gis.location;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface Locations {

	Signal<Location> find(String address);
	
}
