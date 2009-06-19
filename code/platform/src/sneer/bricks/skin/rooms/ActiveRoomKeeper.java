package sneer.bricks.skin.rooms;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface ActiveRoomKeeper {
	
	Signal<String> room();
	Consumer<String> setter();

}
