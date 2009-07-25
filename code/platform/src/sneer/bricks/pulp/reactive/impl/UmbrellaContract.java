package sneer.bricks.pulp.reactive.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.pulp.events.EventSource;
import sneer.foundation.lang.Consumer;

class UmbrellaContract implements Contract {

	
	private final List<Contract> _subContracts;

	
	<T> UmbrellaContract(Consumer<? super T> receiver, EventSource<? extends T>... eventSources) {
		_subContracts = new ArrayList<Contract>(eventSources.length);
		
		for (EventSource<? extends T> source : eventSources)
			_subContracts.add(source.addReceiver(receiver));
	}


	@Override
	public void dispose() {
		for (Contract subContract : _subContracts)
			subContract.dispose();
	}
	
}
