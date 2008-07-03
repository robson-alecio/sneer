package sneer.bricks.port.impl;

import sneer.bricks.port.PortKeeper;
import wheel.io.network.PortNumberRegister;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public class PortKeeperImpl implements PortKeeper {

	private PortNumberRegister _register = new PortNumberRegister(0);
	
	@Override
	public Signal<Integer> port() {
		return _register.output();
	}

	@Override
	public Consumer<Integer> portSetter() {
		return _register.setter();
	}

}
