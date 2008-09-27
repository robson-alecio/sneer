package sneer.pulp.dyndns.ownip.impl;

import java.io.IOException;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.clock.Clock;
import sneer.pulp.datastore.DataStore;
import sneer.pulp.dyndns.checkip.CheckIp;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class OwnIpDiscovererImpl implements OwnIpDiscoverer {
	
	private static final int RETRY_TIME_MINUTES = 11; //The minimum required period is 10 mins, or it is considered abuse.
	private static final String LAST_IP_KEY = "ownIp.ip";
	private static final String LAST_CHECK_TIME_KEY = "ownIp.lastUpdateRequestTime";

	@Inject
	static private Clock _clock;
	
	@Inject
	static private CheckIp _checkip;
	
	@Inject
	static private DataStore _store;
	
	@Inject
	static private BlinkingLights _blinkingLights;
	
	private final Register<String> _ownIp;

	private Light lastLight = null;
	
	private OwnIpDiscovererImpl() {
		_ownIp = new RegisterImpl<String>(restoreIp());
		scheduleNextDiscovery();
	}

	private String restoreIp() {
		String result = _store.get(LAST_IP_KEY);
		lastLight  = _blinkingLights.turnOn(LightType.INFO, "Own Ip Restored: " + result, 10000);
		return result;
	}

	private void tryIpDiscovery() {
		try {
			ipDiscovery();
		} catch (IOException e) {
			turnOnErrorLight(e);
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
		turnOffLastLight();

		_store.set(LAST_CHECK_TIME_KEY, _clock.time());

		final String ip = _checkip.check();
		final String current = _store.get(LAST_IP_KEY);
		
		if (ip.equals(current))
			return;
		
		_store.set(LAST_IP_KEY, ip);
		_ownIp.setter().consume(ip);

		if(ip==null){
			turnOnErrorLight();
			return;
		}
		lastLight  = _blinkingLights.turnOn(LightType.INFO, "Checked Ip: " + ip, 10000);
		
	}

	@Override
	public Signal<String> ownIp() {
		return _ownIp.output();
	}

	private int retryTimeMillis(){
		return RETRY_TIME_MINUTES * 60 * 1000;
	}
	
	private void turnOnErrorLight() {
		turnOnErrorLight(null);
	}
	
	private void turnOnErrorLight(IOException e) {
		String msg = "It was not possible to discover your ip. " +
					 "Sneer will retry again in " + RETRY_TIME_MINUTES + " minutes.";
		if(e==null)
			lastLight  = _blinkingLights.turnOn(LightType.ERROR, msg);
		else
			lastLight  = _blinkingLights.turnOn(LightType.ERROR, msg, e);
	}
	
	private void turnOffLastLight() {
		try {
			if (lastLight!=null 
			&& !lastLight.isOn())	
				_blinkingLights.turnOff(lastLight);
		} catch (RuntimeException e) {
			//ignore
		}
	}
}