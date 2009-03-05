package sneer.pulp.connection.reachability.impl;

import static sneer.brickness.environments.Environments.my;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.clock.Clock;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.connection.reachability.ReachabilitySentinel;
import sneer.pulp.threadpool.Stepper;
import wheel.reactive.impl.Receiver;

class ReachabilitySentinelImpl implements ReachabilitySentinel {
	
	private static final int THIRTY_SECONDS = 30*1000;
	
	private final SocketAccepter _socketAccepter = my(SocketAccepter.class);
	
	private final BlinkingLights _lights = my(BlinkingLights.class);
	
	private static Light _unreachable;
	
	private final Clock _clock = my(Clock.class);	
	
	private long _lastIncomingSocketTime = _clock.time();

	@SuppressWarnings("unused")
	private Receiver<Object> _receiverToAvoidGc;
	
	{
		_receiverToAvoidGc = new Receiver<Object>(_socketAccepter.lastAcceptedSocket()) {@Override public void consume(Object value) {
			updateIncomingSocketTime();
		}};
		
		_clock.wakeUpEvery(THIRTY_SECONDS, new Stepper() { @Override public boolean step() {
			if (_clock.time() - _lastIncomingSocketTime >= THIRTY_SECONDS)
				_lights.turnOnIfNecessary(unreachableLight(), "Unreachable", "You have not received any incoming socket connections recently. Either none of your contacts are online or your machine is unreachable from the internet.");
			return true;
		}});		
	}
	
	private void updateIncomingSocketTime() {
		_lastIncomingSocketTime = _clock.time();
		_lights.turnOffIfNecessary(unreachableLight());
	}
	
	private synchronized Light unreachableLight() {
		if (null == _unreachable)
			_unreachable = _lights.prepare(LightType.WARN);
		return _unreachable;
	}

}