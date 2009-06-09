package sneer.skin.rooms.impl;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.skin.rooms.ActiveRoomKeeper;
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
