package sneer.bricks.pulp.dyndns.ownip.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.clock.Clock;
import sneer.bricks.pulp.datastore.DataStore;
import sneer.bricks.pulp.dyndns.checkip.CheckIp;
import sneer.bricks.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.bricks.pulp.log.Logger;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;

class OwnIpDiscovererImpl implements OwnIpDiscoverer {
	
	private static final int RETRY_TIME_MINUTES = 11; //The minimum required period is 10 mins, or it is considered abuse.
	private static final String LAST_IP_KEY = "ownIp.ip";
	private static final String LAST_CHECK_TIME_KEY = "ownIp.lastUpdateRequestTime";

	private final Clock _clock = my(Clock.class);
	
	private final CheckIp _checkip = my(CheckIp.class);
	
	private final DataStore _store = my(DataStore.class);
	
	private final BlinkingLights _blinkingLights = my(BlinkingLights.class);
	
	private final Register<String> _ownIp;

	private Light _light = null;
	
	
	private OwnIpDiscovererImpl() {
		_ownIp = my(Signals.class).newRegister(restoreIp());
		scheduleNextDiscovery();
	}

	private String restoreIp() {
		String result = _store.get(LAST_IP_KEY);
		my(Logger.class).log("Own Ip Restored: {}", result);
		return result;
	}

	private void tryIpDiscovery() {
		try {
			ipDiscovery();
			turnOffLightIfNecessary();
		} catch (IOException e) {
			turnOnErrorLightIfNecessary(e);
		}
	}

	private void scheduleNextDiscovery() {
		_clock.wakeUpNoEarlierThan(timeForNextDiscovery(), new Runnable() { @Override public void run() {
			tryIpDiscovery();
			scheduleNextDiscovery();
		}});
	}

	private long timeForNextDiscovery() {
		return _store.containsKey(LAST_CHECK_TIME_KEY)
			? _store.getLong(LAST_CHECK_TIME_KEY) + retryTimeMillis()
			: 0;
	}

	protected void ipDiscovery() throws IOException {
		_store.set(LAST_CHECK_TIME_KEY, _clock.time());

		final String ip = _checkip.check();
		final String current = _store.get(LAST_IP_KEY);
		
		if (ip.equals(current))
			return;
		
		_store.set(LAST_IP_KEY, ip);
		_ownIp.setter().consume(ip);

		my(Logger.class).log("Own Ip Discovered: {}", ip);
		
	}

	@Override
	public Signal<String> ownIp() {
		return _ownIp.output();
	}

	private int retryTimeMillis(){
		return RETRY_TIME_MINUTES * 60 * 1000;
	}
	
	private void turnOnErrorLightIfNecessary(IOException e) {
		if (_light != null) return;
		
		String msg = "Sneer will retry again in " + RETRY_TIME_MINUTES + " minutes.";
		
		_light  = _blinkingLights.turnOn(LightType.ERROR, "It was not possible to discover your ip.", msg, e);
	}
	
	private void turnOffLightIfNecessary() {
		if (_light == null) return; 
		_blinkingLights.turnOffIfNecessary(_light);
		_light = null;
	}
}