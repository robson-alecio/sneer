package spikes.legobricks.name.impl;

import spikes.legobricks.name.PortKeeper;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class PortKeeperImpl implements PortKeeper {

	private Register<Integer> _source = new RegisterImpl<Integer>(0);
	
	@Override
	public Signal<Integer> port() {
		return _source.output();
	}

	@Override
	public Omnivore<Integer> portSetter() {
		return _source.setter();
	}

}
