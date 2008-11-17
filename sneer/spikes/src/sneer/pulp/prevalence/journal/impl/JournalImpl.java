package sneer.pulp.prevalence.journal.impl;

import sneer.pulp.prevalence.journal.Journal;
import wheel.io.files.Directory;
import wheel.lang.Consumer;

class JournalImpl implements Journal {

	@SuppressWarnings("unused")
	private final Directory _directory;

	public JournalImpl(Directory directory) {
		_directory = directory;
	}

	@Override
	public Consumer<Object> open(Consumer<Object> previousEntryConsumer) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}
