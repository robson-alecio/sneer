package sneer.pulp.prevalence.impl;

import sneer.pulp.prevalence.Prevayler;
import sneer.pulp.prevalence.StateMachine;

public class PrevaylerImpl implements Prevayler {

	@Override
	public StateMachine makeAcid(StateMachine business, String prevalenceDirectory) {
		StateMachine isolated = new IsolatedStateMachine(business);
		StateMachine durable = new DurableStateMachine(isolated, prevalenceDirectory);
		return new AtomicConsistentStateMachine(durable);
	}

}
