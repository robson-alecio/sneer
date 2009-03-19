package sneer.pulp.own.tagline.impl;

import sneer.pulp.own.tagline.OwnTaglineKeeper;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

class OwnTaglineKeeperImpl implements OwnTaglineKeeper {

	private Register<String> _tagline = new RegisterImpl<String>("[My Tagline]");

	@Override
	public Signal<String> tagline() {
		return _tagline.output();
	}

	@Override
	public Consumer<String> taglineSetter() {
		return _tagline.setter();
	}
}
