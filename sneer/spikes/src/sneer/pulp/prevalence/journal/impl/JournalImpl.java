package sneer.pulp.prevalence.journal.impl;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.prevalence.journal.Journal;
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
