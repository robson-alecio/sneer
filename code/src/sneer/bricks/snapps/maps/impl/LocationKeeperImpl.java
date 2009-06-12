package sneer.bricks.snapps.maps.impl;

import static sneer.foundation.commons.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.snapps.maps.LocationKeeper;

class LocationKeeperImpl implements LocationKeeper {

	private final Register<String> _location = my(Signals.class).newRegister(null);
	private final Register<String> _latitude = my(Signals.class).newRegister(null);
	private final Register<String> _longitude = my(Signals.class).newRegister(null);
	
	@Override
	public Signal<String> location() {
		return _location.output();
	}

	@Override
	public Consumer<String> locationSetter() {
		return _location.setter();
	}
	
	@Override
	public Signal<String> latitude() {
		return _latitude.output();
	}

	@Override
	public Consumer<String> latitudeSetter() {
		return _latitude.setter();
	}
	
	@Override
	public Signal<String> longitude() {
		return _longitude.output();
	}

	@Override
	public Consumer<String> longitudeSetter() {
		return _longitude.setter();
	}
}