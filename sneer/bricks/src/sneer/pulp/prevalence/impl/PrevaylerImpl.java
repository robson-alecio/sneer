package sneer.pulp.prevalence.impl;

import sneer.pulp.prevalence.Prevayler;
import sneer.pulp.prevalence.StateMachine;
import sneer.pulp.prevalence.journal.Journal;

public class PrevaylerImpl implements Prevayler {

	@Override
	public StateMachine makeAcid(StateMachine business, Journal journal) {
		return new DurableStateMachine(journal,
			new AtomicConsistentStateMachine(
				new IsolatedStateMachine(business)));
	}

}
