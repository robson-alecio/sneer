package spikes.snapps.location;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;

public interface LocationKeeper {

	public Signal<String> location();
	public Signal<String> latitude();
	public Signal<String> longitude();
	
	public Consumer<String> locationSetter();
	public Consumer<String> latitudeSetter();
	public Consumer<String> longitudeSetter();
	
}
