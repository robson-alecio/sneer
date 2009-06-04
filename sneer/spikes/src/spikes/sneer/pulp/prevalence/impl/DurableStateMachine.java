package spikes.sneer.pulp.prevalence.impl;

import spikes.sneer.pulp.prevalence.StateMachine;

class DurableStateMachine implements StateMachine {

	@SuppressWarnings("unused")
	private final StateMachine _business;

	DurableStateMachine(StateMachine business, @SuppressWarnings("unused") String prevalenceDirectory) {
		_business = business;
	}

	@Override
	public Object changeState(Object event) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public Object queryState(Object query) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

}
