package sneer.pulp.logging;

import sneer.kernel.container.Brick;
import wheel.io.logging.WheelLogger;
import wheel.lang.Consumer;
import wheel.reactive.EventSource;
import wheel.reactive.lists.ListSignal;

public interface Logger extends WheelLogger, Brick {

	Consumer<String> whiteListEntry();	
	void whiteListEntryAdder(String phrase);
	void whiteListEntryRemover(String phrase);
	ListSignal<String> phrases();
	EventSource<String> loggedMessages();

}
