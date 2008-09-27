package snapps.location;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface LocationKeeper {

	public Signal<String> location();
	public Signal<String> latitude();
	public Signal<String> longitude();
	
	public Omnivore<String> locationSetter();
	public Omnivore<String> latitudeSetter();
	public Omnivore<String> longitudeSetter();
	
}
