package sneer.pulp.log.filter;

import sneer.brickness.Brick;
import sneer.pulp.reactive.collections.ListRegister;

@Brick
public interface LogFilter{

	boolean acceptLog(String message);
	ListRegister<LogWhiteListEntry> whiteListEntries();

}
