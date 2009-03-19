package snapps.location.impl;

import snapps.location.LocationKeeper;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class LocationKeeperImpl implements LocationKeeper {

	private Register<String> _location = new RegisterImpl<String>(null);
	private Register<String> _latitude = new RegisterImpl<String>(null);
	private Register<String> _longitude = new RegisterImpl<String>(null);
	
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
