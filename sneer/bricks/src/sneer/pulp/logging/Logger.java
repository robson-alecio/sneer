package sneer.pulp.logging;

import sneer.kernel.container.Brick;
import wheel.io.logging.WheelLogger;
import wheel.lang.Consumer;
import wheel.reactive.EventSource;
import wheel.reactive.lists.ListRegister;

public interface Logger extends WheelLogger, Brick {

	Consumer<String> whiteListEntry();
	public ListRegister<LogWhiteListEntry> whiteListEntries();
	EventSource<String> loggedMessages();

}
