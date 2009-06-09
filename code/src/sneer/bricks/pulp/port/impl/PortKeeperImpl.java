package sneer.bricks.pulp.port.impl;

import sneer.bricks.hardware.cpu.lang.PickyConsumer;
import sneer.bricks.pulp.port.PortKeeper;
import sneer.bricks.pulp.reactive.Signal;

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
