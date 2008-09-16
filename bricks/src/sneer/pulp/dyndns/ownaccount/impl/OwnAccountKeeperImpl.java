package sneer.pulp.dyndns.ownaccount.impl;

import sneer.pulp.dyndns.ownaccount.Account;
import sneer.pulp.dyndns.ownaccount.OwnAccountKeeper;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class OwnAccountKeeperImpl implements OwnAccountKeeper {

	private Register<Account> _ownAccount = new RegisterImpl<Account>(null);

	@Override
	public Signal<Account> ownAccount() {
		return _ownAccount.output();
	}

	@Override
	public Omnivore<Account> accountSetter() {
		return _ownAccount.setter();
	}

}
