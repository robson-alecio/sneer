package sneer.skin.rooms;

import sneer.brickness.Brick;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface ActiveRoomKeeper extends Brick {
	
	Signal<String> room();
	Consumer<String> setter();

}
