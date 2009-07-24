package sneer.bricks.pulp.events.impl;

import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.PulseSource;
import sneer.bricks.pulp.events.Pulser;

class PulserImpl implements Pulser {

	private final EventNotifier<Object> _delegate = new EventNotifierImpl<Object>();

	@Override
	public PulseSource output() {
		return _delegate.output();
	}

	@Override
	public void sendPulse() {
		_delegate.notifyReceivers(null);
	}

}
