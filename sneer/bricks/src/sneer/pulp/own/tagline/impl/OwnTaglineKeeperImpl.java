package sneer.pulp.own.tagline.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.own.tagline.OwnTaglineKeeper;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.software.lang.Consumer;

class OwnTaglineKeeperImpl implements OwnTaglineKeeper {

	private Register<String> _tagline = my(Signals.class).newRegister("[My Tagline]");

	@Override
	public Signal<String> tagline() {
		return _tagline.output();
	}

	@Override
	public Consumer<String> taglineSetter() {
		return _tagline.setter();
	}
}
