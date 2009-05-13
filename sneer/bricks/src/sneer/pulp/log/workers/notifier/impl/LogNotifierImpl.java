package sneer.pulp.log.workers.notifier.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventNotifiers;
import sneer.pulp.events.EventSource;
import sneer.pulp.log.Logger;
import sneer.pulp.log.formatter.LogFormatter;
import sneer.pulp.log.workers.notifier.LogNotifier;

public class LogNotifierImpl implements LogNotifier {

	private final LogFormatter _formatter = my(LogFormatter.class);
	private final EventNotifier<String> _loggedMessages = my(EventNotifiers.class).create();
	{
		my(Logger.class).delegate(this);
	}
	
	private void log(String msg){
		_loggedMessages.notifyReceivers(msg);
	}

	@Override
	public EventSource<String> loggedMessages() {
		return _loggedMessages.output();
	}
	@Override  public void log(String message, Object... messageInsets) {  log(_formatter.format(message, messageInsets)); }
	@Override  public void log(Throwable throwable, String message, Object... messageInsets) { log(_formatter.format(throwable, message, messageInsets));}
	@Override public void log(Throwable throwable) { log(_formatter.format(throwable)); }
	@Override public void logShort(Exception e, String message, Object... messageInsets) { log(_formatter.formatShort(e, message, messageInsets));}
}
