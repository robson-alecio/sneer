package sneer.tests.adapters;

import sneer.bricks.hardware.io.log.filter.LogFilter;
import sneer.bricks.pulp.reactive.collections.ListRegister;

class LogFilterForTests implements LogFilter {

	@Override
	public boolean acceptLogEntry(String message) {
		return true;
	}

	@Override
	public ListRegister<String> whiteListEntries() {
		throw new UnsupportedOperationException();
	}

}
