package sneer.skin.rooms;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;

@Brick
public interface ActiveRoomKeeper {
	
	Signal<String> room();
	Consumer<String> setter();

}
