package sneer.bricks.network.computers.sockets.reachability.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.clock.timer.Timer;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.network.computers.sockets.accepter.SocketAccepter;
import sneer.bricks.network.computers.sockets.reachability.ReachabilitySentinel;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.port.PortKeeper;
import sneer.foundation.lang.Consumer;

class ReachabilitySentinelImpl implements ReachabilitySentinel {

	private static final int THIRTY_SECONDS = 30*1000;
	private static Light _unreachable;

	private final SocketAccepter _socketAccepter = my(SocketAccepter.class);
	private final BlinkingLights _lights = my(BlinkingLights.class);
	private final Clock _clock = my(Clock.class);	

	private long _lastIncomingSocketTime = _clock.time().currentValue();

	@SuppressWarnings("unused") private final Object _refToAvoidGc;
	@SuppressWarnings("unused") private final WeakContract _timerContract;

	{
		_refToAvoidGc = _socketAccepter.lastAcceptedSocket().addReceiver(new Consumer<Object>() {@Override public void consume(Object value) {
			updateIncomingSocketTime();
		}});

		_timerContract = my(Timer.class).wakeUpEvery(THIRTY_SECONDS, new Steppable() { @Override public void step() {
			if (_clock.time().currentValue() - _lastIncomingSocketTime >= THIRTY_SECONDS)
				_lights.turnOnIfNecessary(unreachableLight(), "Unreachable", "You have not received any incoming sockets recently. Either none of your contacts are online or your machine is unreachable from the internet on your current Sneer TCP Port: " + my(PortKeeper.class).port().currentValue() + ". You can change your Sneer TCP Port using the Menu > Own Info dialog.");
		}});		
	}

	private void updateIncomingSocketTime() {
		_lastIncomingSocketTime = _clock.time().currentValue();
		_lights.turnOffIfNecessary(unreachableLight());
	}

	private synchronized Light unreachableLight() {
		if (null == _unreachable)
			_unreachable = _lights.prepare(LightType.WARN);
		return _unreachable;
	}
}