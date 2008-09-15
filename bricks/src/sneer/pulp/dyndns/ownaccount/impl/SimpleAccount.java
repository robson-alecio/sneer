package sneer.pulp.dyndns.ownaccount.impl;

import sneer.pulp.dyndns.ownaccount.Account;

class SimpleAccount implements Account {

	private final String _host;
	private final String _password;
	private final String _user;

	public SimpleAccount(String host, String user, String password) {
		_host = host;
		_user = user;
		_password = password;
	}

	@Override
	public String host() {
		return _host;
	}

	@Override
	public String password() {
		return _password;
	}

	@Override
	public String user() {
		return _user;
	}

}
