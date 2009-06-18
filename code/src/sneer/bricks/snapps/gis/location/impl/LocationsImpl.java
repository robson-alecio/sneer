package sneer.bricks.snapps.gis.location.impl;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.snapps.gis.location.Location;
import sneer.bricks.snapps.gis.location.Locations;
import sneer.foundation.lang.Consumer;

class LocationsImpl implements Locations {

	@Override
	public Signal<Location> find(final String address) {
		return new Signal<Location>(){
			LocationImpl location = new LocationImpl(address);
			Signal<Location> signal = location.location(); 
			@Override public Location currentValue() { return signal.currentValue(); }
			@Override public void addReceiver(Consumer<? super Location> receiver) { signal.addReceiver(receiver); }
			@Override public void removeReceiver(Object receiver) { signal.removeReceiver(receiver); }
		};
	}
}
