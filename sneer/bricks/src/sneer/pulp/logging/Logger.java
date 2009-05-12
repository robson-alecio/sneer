package sneer.pulp.logging;

import sneer.brickness.Brick;
import sneer.pulp.events.EventSource;
import sneer.pulp.reactive.collections.ListRegister;

@Brick
public interface Logger {

	void log(String message, Object... messageInsets);
	void logShort(Exception e, String message, Object... insets);
	void log(Throwable throwable, String message, Object... messageInsets) ;
	void log(Throwable throwable);

	//Refactor: break the methods below into a separate LogDashboard brick.
	
	ListRegister<LogWhiteListEntry> whiteListEntries();
	EventSource<String> loggedMessages();
	/**Stops rethrowing logged throwables. Default operation mode is to rethrow throwables instead of logging them for the benefit of unit tests.*/
	void enterRobustMode();

}
