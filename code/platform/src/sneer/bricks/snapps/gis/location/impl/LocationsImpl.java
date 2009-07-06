package sneer.bricks.snapps.gis.location.impl;

import sneer.bricks.snapps.gis.location.Location;
import sneer.bricks.snapps.gis.location.Locations;
import sneer.foundation.lang.Consumer;

class LocationsImpl implements Locations {

	@Override
	public void find(final String address, Consumer<Location> receiver) {
		new LocationImpl(address, receiver);
	}
}
