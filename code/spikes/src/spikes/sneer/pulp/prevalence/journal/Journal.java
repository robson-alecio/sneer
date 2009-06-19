package spikes.sneer.pulp.prevalence.journal;

import sneer.foundation.lang.Consumer;

public interface Journal {

	/**
	 * @param previousEntryConsumer to which all existing entries in the journal will be passed before this method returns.
	 * @return a consumer for appending entries to the journal.
	 */
	Consumer<Object> open(String directory, Consumer<Object> previousEntryConsumer);
	
}
