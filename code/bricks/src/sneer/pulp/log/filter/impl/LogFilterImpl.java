package sneer.pulp.log.filter.impl;

import static sneer.commons.environments.Environments.my;

import java.util.List;

import sneer.pulp.log.filter.LogFilter;
import sneer.pulp.reactive.collections.CollectionSignals;
import sneer.pulp.reactive.collections.ListRegister;

class LogFilterImpl implements LogFilter {

	private final ListRegister<String> _phrases = my(CollectionSignals.class).newListRegister();
	{
		_phrases.add("[");
	}
	
	@Override
	public ListRegister<String> whiteListEntries() {
		return _phrases;
	}

	@Override
	public boolean acceptLogEntry(String message) {
		List<String> whiteList = _phrases.output().currentElements();
		for (String entry : whiteList) 
			if(message.contains(entry)) return true;
		
		return false;
	}	
}
