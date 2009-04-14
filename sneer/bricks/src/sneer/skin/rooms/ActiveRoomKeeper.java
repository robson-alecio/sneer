package sneer.skin.rooms;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;
import sneer.software.lang.Consumer;

public interface ActiveRoomKeeper extends OldBrick {
	
	Signal<String> room();
	Consumer<String> setter();

}
