package sneer.bricks.pulp.events.pulsers.impl;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.pulp.events.pulsers.PulseSource;
import sneer.bricks.pulp.events.pulsers.Pulser;
import sneer.bricks.pulp.events.pulsers.Pulsers;

class PulsersImpl implements Pulsers {

	@Override
	public Pulser newInstance() {
		return new PulserImpl();
	}

	@Override
	public WeakContract receive(Runnable receiver, PulseSource... multipleSources) {
		return new UmbrellaContract(receiver, multipleSources);
	}

}