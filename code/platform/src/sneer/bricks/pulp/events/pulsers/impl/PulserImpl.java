package sneer.bricks.pulp.events.pulsers.impl;

import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.events.pulsers.PulseSource;
import sneer.bricks.pulp.events.pulsers.Pulser;
import static sneer.foundation.environments.Environments.my;

class PulserImpl implements Pulser {

	private final EventNotifier<Object> _delegate = my(EventNotifiers.class).newInstance();

	@Override
	public PulseSource output() {
		return _delegate.output();
	}

	@Override
	public void sendPulse() {
		_delegate.notifyReceivers(null);
	}

}
