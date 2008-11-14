package sneer.pulp.port.impl;

import sneer.pulp.port.PortKeeper;
import wheel.io.network.PortNumberRegister;
import wheel.lang.PickyConsumer;
import wheel.reactive.Signal;

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
