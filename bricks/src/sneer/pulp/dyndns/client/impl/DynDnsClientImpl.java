package sneer.pulp.dyndns.client.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.dyndns.ownaccount.OwnAccountKeeper;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.dyndns.updater.Updater;
import wheel.reactive.impl.Receiver;

class DynDnsClientImpl implements DynDnsClient {
	
	@Inject
	static private OwnIpDiscoverer _ownIpDiscoverer;
	
	@Inject
	static private OwnAccountKeeper _ownAccountKeeper;
	
	@Inject
	static private Updater _updater;
	
	final Receiver<String> _ownIpReceiver = new Receiver<String>(_ownIpDiscoverer.ownIp()) { @Override public void consume(String value) {
		update(value);
	}};
//	
//	final Receiver<Account> _ownAccountReceiver = new Receiver<Account>(_ownAccountKeeper.ownAccount()) { @Override public void consume(Account value) {
//		
//	}};

	protected void update(String ip) {
		_updater.update(_ownAccountKeeper.ownAccount().currentValue(), ip);
	}

}
