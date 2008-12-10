package sneer.pulp.dyndns.client.impl;

import java.io.IOException;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.clock.Clock;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.dyndns.updater.BadAuthException;
import sneer.pulp.dyndns.updater.InvalidHostException;
import sneer.pulp.dyndns.updater.RedundantUpdateException;
import sneer.pulp.dyndns.updater.Updater;
import sneer.pulp.dyndns.updater.UpdaterException;
import sneer.pulp.propertystore.PropertyStore;
import sneer.pulp.threadpool.ThreadPool;
import wheel.reactive.impl.Receiver;

class DynDnsClientImpl implements DynDnsClient {
	
	private static final String LAST_IP_KEY = "dyndns.lastIp";
	private static final String LAST_HOST_KEY = "dyndns.lastHost";

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
	static private ThreadPool _threadPool;
	
	@Inject
	static private Clock _clock;
	
	private final Object _stateMonitor = new Object();
	private State _state = new Happy();
	private final Light _light = _blinkingLights.prepare(LightType.ERROR);
	
	final Receiver<Object> _reactionTrigger = new Receiver<Object>() { @Override public void consume(Object ignored) {
		synchronized (_stateMonitor) {
			_state = _state.react();
		}
	}};
	
	{
		_reactionTrigger.addToSignal(_ownAccountKeeper.ownAccount());
		_reactionTrigger.addToSignal(_ownIpDiscoverer.ownIp());
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
			Runnable toRun = new Runnable(){ @Override public void run() {
				State state = submitUpdateRequest(currentAccount(), currentIp());
				synchronized (_stateMonitor) {
					_state = state;
					if (_isReactionPending)
						_state = _state.react();
				}
			}};
			_threadPool.registerActor(toRun);
			
		}
		
		private State submitUpdateRequest(final DynDnsAccount account, String ip) {
			try {
				_updater.update(account.host, account.dynDnsUser, account.password, ip);
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
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
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
	
		Waiting(IOException e) {
			super("Dyndns Error:", "It was not possible to connect to the dyndns server. Sneer will retry again in " + retryTimeoutInMinutes + " minutes.", e);
			addAlarm(retryTimeoutImMillis());
		}

		private void addAlarm(long millisFromNow) {
			_clock.wakeUpInAtLeast((int)millisFromNow, new Runnable() { @Override public void run() {
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