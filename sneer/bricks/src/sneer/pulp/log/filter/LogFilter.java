package sneer.pulp.log.filter;

import sneer.brickness.Brick;
import sneer.pulp.log.Filter;
import sneer.pulp.reactive.collections.ListRegister;

@Brick
public interface LogFilter extends Filter{

	ListRegister<LogWhiteListEntry> whiteListEntries();

}
