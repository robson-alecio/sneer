package sneer.pulp.dyndns.client.impl;

import java.io.IOException;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.clock.Clock;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
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
	static private DynDnsAccountKeeper _ownAccountKeeper;
	
	@Inject
	static private Updater _updater;
	
	@Inject
	static private PropertyStore _propertyStore;
	
	@Inject
	static private BlinkingLights _blinkingLights;
	
	@Inject
	static private Clock _clock;
	
	private State _state = new Happy();
	private final Object _stateMonitor = new Object();
	
	final Receiver<DynDnsAccount> _ownAccountReceiver = new Receiver<DynDnsAccount>(_ownAccountKeeper.ownAccount()) { @Override public void consume(DynDnsAccount account) {
		synchronized (_stateMonitor) {
			if (account == null) return;
			_state = _state.reactTo(account);
		}
	}};
	
	final Receiver<String> _ownIpReceiver = new Receiver<String>(_ownIpDiscoverer.ownIp()) { @Override public void consume(String ownIp) {
		synchronized (_stateMonitor) {
			if (ownIp == null) return;
			_state = _state.reactTo(ownIp);
		}
	}};

	private State submitUpdateRequest(final DynDnsAccount account, String ip) {
		try {
			_updater.update(account.host, account.dynDnsUser, account.password, ip);
			recordLastIp(ip);
			return new Happy();
		} catch (BadAuthException e) {
			return new BadAuthState(e);
		} catch (IOException e) {
			return new Waiting(e);
		} catch (UpdaterException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	abstract class State {
		
		abstract State reactTo(String ownIpNotification);
		abstract State reactTo(DynDnsAccount accountNotification);
		abstract State reactToAlarm();
		
	}
	
	private final class Happy extends State {
		
		@Override
		State reactTo(String ip) {
			if (ip.equals(lastIp())) return this;
			
			return submitUpdateRequest(currentAccount(), ip);
		}

		@Override
		State reactTo(DynDnsAccount accountNotification) {
			return this;
		}

		@Override
		State reactToAlarm() {
			throw new IllegalStateException();
		}

	}
	
	private abstract class Sad extends State {
		
		private final Light _light;
		
		Sad(String message, Exception e) {
			_light = _blinkingLights.turnOn(LightType.ERROR, message, e);
		}

		protected State retry() {
			_light.turnOff();
			return submitUpdateRequest(currentAccount(), currentIp());
		}
	}
	
	private final class Waiting extends Sad {
		
		static final int retryTimeoutInMinutes = 5;
		
		Waiting(IOException e) {
			super("It was not possible to connect to the dyndns server. Sneer will retry again in " + retryTimeoutInMinutes + " minutes.", e);
			_clock.addAlarm(retryTimeoutInMinutes * 60 * 1000, new Runnable() { @Override public void run() {
				synchronized (_stateMonitor) {
					_state = _state.reactToAlarm();
				}
			}});
		}
		
		@Override
		State reactToAlarm() {
			return retry();
		}

		@Override
		State reactTo(String ownIpNotification) {
			return this;
		}

		@Override
		State reactTo(DynDnsAccount accountNotification) {
			return this;
		}

	}
	
	private final class BadAuthState extends Sad {

		BadAuthState(BadAuthException e) {
			super(e.getHelp(), e);
		}

		@Override
		State reactTo(DynDnsAccount newAccount) {
			return retry();
		}

		@Override
		State reactTo(String ownIpNotification) {
			return this;
		}

		@Override
		State reactToAlarm() {
			throw new IllegalStateException();
		}
	}
	
	private void recordLastIp(String ip) {
		_propertyStore.set(LAST_IP_KEY, ip);
	}

	private String lastIp() {
		return _propertyStore.get(LAST_IP_KEY);
	}

	private DynDnsAccount currentAccount() {
		return _ownAccountKeeper.ownAccount().currentValue();
	}

	private String currentIp() {
		return _ownIpDiscoverer.ownIp().currentValue();
	}

}
