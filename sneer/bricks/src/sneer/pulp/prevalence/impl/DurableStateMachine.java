package sneer.pulp.prevalence.impl;

import sneer.pulp.prevalence.StateMachine;
import sneer.pulp.prevalence.journal.Journal;

public class DurableStateMachine implements StateMachine {

	@SuppressWarnings("unused")
	private final StateMachine _business;
	@SuppressWarnings("unused")
	private final Journal _journal;

	public DurableStateMachine(Journal journal, StateMachine business) {
		_business = business;
		_journal = journal;
	}

	@Override
	public Object changeState(Object event) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public Object queryState(Object query) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}
