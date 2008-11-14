package sneer.pulp.prevalence.journal;

import wheel.lang.Consumer;

public interface Journal {

	/** All existing entries will be passed to the entryConsumer. */
	Consumer<Object> open(Consumer<Object> entryConsumer);
	
}
