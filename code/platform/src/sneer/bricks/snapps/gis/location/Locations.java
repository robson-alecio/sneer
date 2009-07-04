package sneer.bricks.snapps.gis.location;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface Locations {

	void find(String address, Consumer<Location> receiver);
	
}
