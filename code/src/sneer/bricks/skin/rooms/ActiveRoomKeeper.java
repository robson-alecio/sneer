package sneer.bricks.skin.rooms;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface ActiveRoomKeeper {
	
	Signal<String> room();
	Consumer<String> setter();

}
