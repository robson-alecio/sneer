package sneer.bricks.pulp.dyndns.client.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.hardware.clock.timer.Timer;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.dyndns.client.DynDnsClient;
import sneer.bricks.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.bricks.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import sneer.bricks.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.bricks.pulp.dyndns.updater.BadAuthException;
import sneer.bricks.pulp.dyndns.updater.InvalidHostException;
import sneer.bricks.pulp.dyndns.updater.RedundantUpdateException;
import sneer.bricks.pulp.dyndns.updater.Updater;
import sneer.bricks.pulp.dyndns.updater.UpdaterException;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.events.pulsers.Pulsers;
import sneer.bricks.pulp.propertystore.PropertyStore;

class DynDnsClientImpl implements DynDnsClient {

	private static final String LAST_IP_KEY = "dyndns.lastIp";
	private static final String LAST_HOST_KEY = "dyndns.lastHost";

	private final OwnIpDiscoverer _ownIpDiscoverer = my(OwnIpDiscoverer.class);
	private final DynDnsAccountKeeper _ownAccountKeeper = my(DynDnsAccountKeeper.class);
	private final Updater _updater = my(Updater.class);
	private final PropertyStore _propertyStore = my(PropertyStore.class);
	private final BlinkingLights _blinkingLights = my(BlinkingLights.class);
	private final Threads _threads = my(Threads.class);
	private final Object _stateMonitor = new Object();
	private final Light _light = _blinkingLights.prepare(LightType.ERROR);

	private State _state = new Happy();

	@SuppressWarnings("unused")	private final WeakContract _referenceToAvoidGc;

	DynDnsClientImpl() {
		_referenceToAvoidGc = my(Pulsers.class).receive(new Runnable() { @Override public void run() {
			synchronized (_stateMonitor) {
				_state = _state.react();
			}
		}}, relevantSignals());
	}

	abstract class State {
		
		abstract State react();
		abstract State wakeUp();
		
	}
	
	private final class Happy extends State {
		
		Happy() {
			if (_light == null) return;
			_blinkingLights.turnOffIfNecessary(_light);
		}
		
		@Override
		State react() {
			if (currentIp() == null) return this;
			if (currentHost() == null) return this;
			
			if (currentIp().equals(lastIp()) && currentHost().equals(lastHost())) return this;
			
			return new Requesting();
		}

		@Override
		State wakeUp() {
			throw new IllegalStateException();
		}
	}
	
	private class Requesting extends State {

		private boolean _isReactionPending = false;

		Requesting() {
			_threads.startDaemon("DynDns Requesting", new Runnable() { @Override public void run() {
				State state = submitUpdateRequest(currentAccount(), currentIp());
				synchronized (_stateMonitor) {
					_state = state;
					if (_isReactionPending)
						_state = _state.react();
				}
			}});
		}

		private State submitUpdateRequest(final DynDnsAccount account, String ip) {
			try {
				_updater.update(account.host, account.user, account.password, ip);
				recordLastSuccess(ip, account.host);
				return new Happy();
			} catch (RedundantUpdateException e) {
				Happy happy = new Happy();
				_blinkingLights.turnOnIfNecessary(_light, e);
				return happy;
			} catch (BadAuthException e) {
				return new BadAccountState(e, account);
			} catch (InvalidHostException e) {
				return new BadAccountState(e, account);
			} catch (IOException e) {
				return new Waiting(e);
			} catch (UpdaterException e) {
				throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			}
		}
		
		@Override
		State react() {
			_isReactionPending = true;
			return this;
		}

		@Override
		State wakeUp() {
			throw new IllegalStateException();
		}
	}
	
	private abstract class Sad extends State {
		
		Sad(String caption, String message, Exception e) {
			refreshErrorLight(caption, message, e);
		}

		protected State retry() {
			return new Requesting();
		}
		
		private void refreshErrorLight(String caption, String message, Exception e) {
			_blinkingLights.turnOffIfNecessary(_light);
			_blinkingLights.turnOnIfNecessary(_light, caption, message, e);
		}
	}
	
	private final class Waiting extends Sad {
		
		static final int retryTimeoutInMinutes = 5;
		@SuppressWarnings("unused") private WeakContract _contractToWakeUp;
	
		
		Waiting(IOException e) {
			super("Dyndns Error:", "It was not possible to connect to the dyndns server. Sneer will retry again in " + retryTimeoutInMinutes + " minutes.", e);
			addAlarm(retryTimeoutImMillis());
		}

		
		private void addAlarm(long millisFromNow) {
			_contractToWakeUp = my(Timer.class).wakeUpInAtLeast((int)millisFromNow, new Runnable() { @Override public void run() {
				synchronized (_stateMonitor) {
					_state = _state.wakeUp();
				}
			}});
		}

		private int retryTimeoutImMillis() {
			return retryTimeoutInMinutes * 60 * 1000;
		}
		
		@Override
		State wakeUp() {
			return retry();
		}

		@Override
		State react() {
			return this;
		}

	}
	
	
	private final class BadAccountState extends Sad {

		private final DynDnsAccount _lastAccount;

		BadAccountState(UpdaterException e, DynDnsAccount account) {
			super(e.getMessage(), e.getHelp(), e);
			_lastAccount = account;
		}

		@Override
		State react() {
			if (_lastAccount.equals(currentAccount())) return this;
			return retry();
		}

		@Override
		State wakeUp() {
			throw new IllegalStateException();
		}
	}
	
	private void recordLastSuccess(String ip, String host) {
		_propertyStore.set(LAST_IP_KEY, ip);
		_propertyStore.set(LAST_HOST_KEY, host);
	}
	
	private EventSource<Object>[] relevantSignals() {
		return new EventSource[] {
			_ownAccountKeeper.ownAccount(),
			_ownIpDiscoverer.ownIp()
		};
	}

	private String lastIp() {
		return _propertyStore.get(LAST_IP_KEY);
	}
	private Object lastHost() {
		return _propertyStore.get(LAST_HOST_KEY);
	}
	

	private DynDnsAccount currentAccount() {
		return _ownAccountKeeper.ownAccount().currentValue();
	}

	private String currentIp() {
		return _ownIpDiscoverer.ownIp().currentValue();
	}
	
	private String currentHost() {
		if (currentAccount() == null) return null;
		return currentAccount().host;
	}
}