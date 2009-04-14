package snapps.location;

import sneer.pulp.reactive.Signal;
import sneer.software.lang.Consumer;

public interface LocationKeeper {

	public Signal<String> location();
	public Signal<String> latitude();
	public Signal<String> longitude();
	
	public Consumer<String> locationSetter();
	public Consumer<String> latitudeSetter();
	public Consumer<String> longitudeSetter();
	
}
