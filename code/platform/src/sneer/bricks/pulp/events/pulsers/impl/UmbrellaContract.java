package sneer.bricks.pulp.events.pulsers.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.pulp.events.pulsers.PulseSource;

class UmbrellaContract implements WeakContract {

	
	private final List<WeakContract> _subContracts;

	
	<T> UmbrellaContract(Runnable receiver, PulseSource... sources) {
		_subContracts = new ArrayList<WeakContract>(sources.length);
		
		for (PulseSource source : sources)
			_subContracts.add(source.addPulseReceiver(receiver));
	}


	@Override
	public void dispose() {
		for (WeakContract subContract : _subContracts)
			subContract.dispose();
	}
	
}
