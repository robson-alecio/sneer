package sneer.pulp.own.name.impl;

import sneer.pulp.own.name.OwnNameKeeper;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class OwnNameKeeperImpl implements OwnNameKeeper {

	private Register<String> _name = new RegisterImpl<String>(null);
	
	@Override
	public Signal<String> name() {
		return _name.output();
	}

	@Override
	public Omnivore<String> nameSetter() {
		return _name.setter();
	}

	@Override
	public void setName(String name) {
		nameSetter().consume(name);
	}

	@Override
	public String getName() {
		return name().currentValue();
	}
}
