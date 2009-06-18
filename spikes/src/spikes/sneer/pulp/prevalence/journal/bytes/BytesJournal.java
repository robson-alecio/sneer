package spikes.sneer.pulp.prevalence.journal.bytes;

import sneer.foundation.lang.Consumer;

public interface BytesJournal {

	/**
	 * @param previousEntryConsumer to which all existing entries in the journal will be passed before this method returns.
	 * @return a consumer for appending entries to the journal.
	 */
	Consumer<byte[]> open(String directory, Consumer<byte[]> previousEntryConsumer);
	
}
