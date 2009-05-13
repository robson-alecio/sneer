package sneer.pulp.log.filter.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.log.Logger;
import sneer.pulp.log.filter.LogFilter;
import sneer.pulp.log.filter.LogWhiteListEntry;
import sneer.pulp.reactive.collections.CollectionSignals;
import sneer.pulp.reactive.collections.ListRegister;

class LogFilterImpl implements LogFilter {

	private final ListRegister<LogWhiteListEntry> _phrases = my(CollectionSignals.class).newListRegister();
	
	LogFilterImpl(){
		my(Logger.class).filter(this);
	}
	
	@Override
	public ListRegister<LogWhiteListEntry> whiteListEntries() {
		return _phrases;
	}

	@Override
	public boolean acceptLog(String message) {
		//Implement
		return true;
	}	
}
