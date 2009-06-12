package sneer.bricks.snapps.maps;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface LocationKeeper {

	Signal<String> location();
	Signal<String> latitude();
	Signal<String> longitude();
	
	Consumer<String> locationSetter();
	Consumer<String> latitudeSetter();
	Consumer<String> longitudeSetter();
	
}