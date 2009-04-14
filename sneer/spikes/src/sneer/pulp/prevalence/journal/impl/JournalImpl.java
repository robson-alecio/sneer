package sneer.pulp.prevalence.journal.impl;

import sneer.pulp.prevalence.journal.Journal;
import sneer.software.lang.Consumer;
import wheel.io.files.Directory;

class JournalImpl implements Journal {

	@SuppressWarnings("unused")
	private final Directory _directory;

	public JournalImpl(Directory directory) {
		_directory = directory;
	}

	@Override
	public Consumer<Object> open(String directory, Consumer<Object> previousEntryConsumer) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

}
