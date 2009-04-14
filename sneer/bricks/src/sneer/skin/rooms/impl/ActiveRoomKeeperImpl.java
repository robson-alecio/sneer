package sneer.skin.rooms.impl;

import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.skin.rooms.ActiveRoomKeeper;
import sneer.software.lang.Consumer;
import static sneer.commons.environments.Environments.my;

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
