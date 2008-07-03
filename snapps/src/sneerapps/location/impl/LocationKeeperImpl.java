package sneerapps.location.impl;

import sneerapps.location.LocationKeeper;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;
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
	public Omnivore<String> locationSetter() {
		return _location.setter();
	}
	
	@Override
	public Signal<String> latitude() {
		return _latitude.output();
	}

	@Override
	public Omnivore<String> latitudeSetter() {
		return _latitude.setter();
	}
	
	@Override
	public Signal<String> longitude() {
		return _longitude.output();
	}

	@Override
	public Omnivore<String> longitudeSetter() {
		return _longitude.setter();
	}

}
