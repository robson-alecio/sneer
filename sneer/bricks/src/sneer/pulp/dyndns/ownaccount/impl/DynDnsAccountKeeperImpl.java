package sneer.pulp.dyndns.ownaccount.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import wheel.lang.Consumer;

class DynDnsAccountKeeperImpl implements DynDnsAccountKeeper {

	private Register<DynDnsAccount> _ownAccount = my(Signals.class).newRegister(null);

	@Override
	public Signal<DynDnsAccount> ownAccount() {
		return _ownAccount.output();
	}

	@Override
	public Consumer<DynDnsAccount> accountSetter() {
		return _ownAccount.setter();
	}

}
