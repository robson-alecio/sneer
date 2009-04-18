package sneer.skin.rooms;

import sneer.brickness.OldBrick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;

public interface ActiveRoomKeeper extends OldBrick {
	
	Signal<String> room();
	Consumer<String> setter();

}
