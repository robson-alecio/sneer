package sneer.pulp.log.filter;

import sneer.brickness.Brick;
import sneer.pulp.reactive.collections.ListRegister;

@Brick
public interface LogFilter {

	ListRegister<LogWhiteListEntry> whiteListEntries();

}
