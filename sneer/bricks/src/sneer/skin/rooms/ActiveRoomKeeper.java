package sneer.skin.rooms;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public interface ActiveRoomKeeper extends Brick {
	
	Signal<String> room();
	Consumer<String> setter();

}
