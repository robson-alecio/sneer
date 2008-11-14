package sneer.pulp.prevalence;

import sneer.pulp.prevalence.journal.Journal;

public interface Prevayler {

	StateMachine makeAcid(StateMachine business, Journal journal);
	
}
