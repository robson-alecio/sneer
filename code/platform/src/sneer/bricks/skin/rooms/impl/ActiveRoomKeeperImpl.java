package sneer.bricks.skin.rooms.impl;

import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.rooms.ActiveRoomKeeper;
import sneer.foundation.lang.Consumer;
import static sneer.foundation.environments.Environments.my;

class ActiveRoomKeeperImpl implements ActiveRoomKeeper {

	private final Register<String> _register = my(Signals.class).newRegister("");

	@Override
	public Signal<String> room() {
		return _register.output();
	}

	@Override
	public Consumer<String> setter() {
		return _register.setter();
	}

}
