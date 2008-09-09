package sneer.pulp.dyndns.client.impl;

import java.io.IOException;

import sneer.kernel.container.Inject;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.dyndns.ownaccount.Account;
import sneer.pulp.dyndns.ownaccount.OwnAccountKeeper;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.dyndns.updater.BadAuthException;
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
	
	private State _state = new DefaultState();
	
	final Receiver<String> _ownIpReceiver = new Receiver<String>(_ownIpDiscoverer.ownIp()) { @Override public void consume(String value) {
		_state.update(value);
	}};
//	
//	final Receiver<Account> _ownAccountReceiver = new Receiver<Account>(_ownAccountKeeper.ownAccount()) { @Override public void consume(Account value) {
//		
//	}};
	
	interface State {
		void update(String ip);
	}
	
	private final class DefaultState implements State {
		public void update(String ip) {
			if (ip.equals(lastIp())) return;
			
			final Account account = _ownAccountKeeper.ownAccount().currentValue();
			try {
				_updater.update(account.host(), account.user(), account.password(), ip);
				recordLastIp(ip);
			} catch (BadAuthException e) {
//				notifyUser(e);
				enterState(new BadAuthState());
				return;
			} catch (IOException e) {
				// retry later
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			} catch (UpdaterException e) {
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			}
		}
	}
	
	private final class BadAuthState implements State {
		@Override
		public void update(String ip) {
			// log("IP change ignored. Waiting for new authentication information.");
		}
	}
	
	private void recordLastIp(String ip) {
		_propertyStore.set(LAST_IP_KEY, ip);
	}

	public void enterState(State state) {
		_state = state;
	}

	private String lastIp() {
		return _propertyStore.get(LAST_IP_KEY);
	}
}
