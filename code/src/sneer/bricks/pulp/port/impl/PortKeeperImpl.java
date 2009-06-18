package sneer.bricks.pulp.port.impl;

import sneer.bricks.pulp.port.PortKeeper;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.lang.PickyConsumer;

class PortKeeperImpl implements PortKeeper {

	private PortNumberRegister _register = new PortNumberRegister(0);
	
	@Override
	public Signal<Integer> port() {
		return _register.output();
	}

	@Override
	public PickyConsumer<Integer> portSetter() {
		return _register.setter();
	}

}
