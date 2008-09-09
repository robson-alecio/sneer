package sneer.pulp.dyndns.client.impl;

import java.io.IOException;

import sneer.kernel.container.Inject;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.dyndns.ownaccount.Account;
import sneer.pulp.dyndns.ownaccount.OwnAccountKeeper;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.dyndns.updater.Updater;
import sneer.pulp.dyndns.updater.UpdaterException;
import sneer.pulp.propertystore.PropertyStore;
import wheel.reactive.impl.Receiver;

class DynDnsClientImpl implements DynDnsClient {
	
	private static final String LAST_IP_KEY = "dyndns.lastIp";

	@Inject
	static private OwnIpDiscoverer _ownIpDiscoverer;
	
	@Inject
	static private OwnAccountKeeper _ownAccountKeeper;
	
	@Inject
	static private Updater _updater;
	
	@Inject
	static private PropertyStore _propertyStore;
	
	final Receiver<String> _ownIpReceiver = new Receiver<String>(_ownIpDiscoverer.ownIp()) { @Override public void consume(String value) {
		update(value);
	}};
//	
//	final Receiver<Account> _ownAccountReceiver = new Receiver<Account>(_ownAccountKeeper.ownAccount()) { @Override public void consume(Account value) {
//		
//	}};

	protected void update(String ip) {
		if (ip.equals(lastIp())) return;
		
		final Account account = _ownAccountKeeper.ownAccount().currentValue();
		try {
			_updater.update(account.host(), account.user(), account.password(), ip);
		} catch (UpdaterException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
		
		recordLastIp(ip);
	}

	private void recordLastIp(String ip) {
		_propertyStore.set(LAST_IP_KEY, ip);
	}

	private String lastIp() {
		return _propertyStore.get(LAST_IP_KEY);
	}

}
