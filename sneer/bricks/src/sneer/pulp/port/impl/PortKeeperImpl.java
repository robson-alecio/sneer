package sneer.pulp.port.impl;

import sneer.pulp.port.PortKeeper;
import sneer.pulp.reactive.Signal;
import wheel.io.network.PortNumberRegister;
import wheel.lang.PickyConsumer;

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
