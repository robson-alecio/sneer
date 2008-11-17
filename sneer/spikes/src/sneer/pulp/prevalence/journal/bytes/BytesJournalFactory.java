package sneer.pulp.prevalence.journal.bytes;

import wheel.io.files.Directory;

public interface BytesJournalFactory {

	BytesJournal createJournal(Directory directory);
	
}
