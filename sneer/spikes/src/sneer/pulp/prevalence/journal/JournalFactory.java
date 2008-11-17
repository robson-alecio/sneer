package sneer.pulp.prevalence.journal;

import wheel.io.files.Directory;

public interface JournalFactory {

	Journal createJournal(Directory directory);
	
}
