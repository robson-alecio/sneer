package sneer.pulp.prevalence.journal.impl;

import sneer.pulp.prevalence.journal.Journal;
import sneer.pulp.prevalence.journal.JournalFactory;
import wheel.io.files.Directory;

class JournalFactoryImpl implements JournalFactory {

	@Override
	public Journal createJournal(Directory directory) {
		return new JournalImpl(directory);
	}

}