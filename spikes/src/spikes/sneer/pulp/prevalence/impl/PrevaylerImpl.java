package spikes.sneer.pulp.prevalence.impl;

import spikes.sneer.pulp.prevalence.Prevayler;
import spikes.sneer.pulp.prevalence.StateMachine;

public class PrevaylerImpl implements Prevayler {

	@Override
	public StateMachine makeAcid(StateMachine business, String prevalenceDirectory) {
		StateMachine isolated = new IsolatedStateMachine(business);
		StateMachine durable = new DurableStateMachine(isolated, prevalenceDirectory);
		return new AtomicConsistentStateMachine(durable);
	}

}
