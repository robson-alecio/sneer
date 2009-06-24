package sneer.bricks.hardware.io.log.filter;

import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.foundation.brickness.Brick;

@Brick
public interface LogFilter{

	boolean acceptLogEntry(String message);
	ListRegister<String> whiteListEntries();

}
