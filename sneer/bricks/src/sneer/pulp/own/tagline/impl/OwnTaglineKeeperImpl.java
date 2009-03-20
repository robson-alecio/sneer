package sneer.pulp.own.tagline.impl;

import sneer.pulp.own.tagline.OwnTaglineKeeper;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.impl.RegisterImpl;
import wheel.lang.Consumer;

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
