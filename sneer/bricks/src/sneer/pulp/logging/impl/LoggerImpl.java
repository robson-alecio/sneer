package sneer.pulp.logging.impl;

import static sneer.brickness.Environments.my;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.pulp.logging.LogWhiteListEntry;
import sneer.pulp.tuples.TupleSpace;
import wheel.io.logging.impl.WheelLoggerImpl;
import wheel.lang.Consumer;
import wheel.reactive.EventNotifier;
import wheel.reactive.EventSource;
import wheel.reactive.impl.EventNotifierImpl;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.impl.ListRegisterImpl;

class LoggerImpl extends WheelLoggerImpl implements sneer.pulp.logging.Logger, Consumer<LogWhiteListEntry> {

	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	private final TupleFilterManager _filter = my(TupleFilterManager.class); 	{
		_filter.block(LogWhiteListEntry.class);
	}
	
	private final ListRegisterImpl<LogWhiteListEntry> _phrases = new ListRegisterImpl<LogWhiteListEntry>();
	
	private final EventNotifier<String> _loggedMessages = new EventNotifierImpl<String>();	

	LoggerImpl() {
		_tupleSpace.addSubscription(LogWhiteListEntry.class, this);
		_tupleSpace.keep(LogWhiteListEntry.class);
	}
	
	@Override
	public void consume(LogWhiteListEntry value) {
		_phrases.adder().consume(value);
	}
	
	@Override
	public ListRegister<LogWhiteListEntry> whiteListEntries() {
		return _phrases;
	}	

	@Override
	public Consumer<String> whiteListEntry() {
		return new Consumer<String>() { @Override public void consume(String phrase) {
			publish(phrase);
		}};
	}
	
	private void publish(String phrase) {
		_tupleSpace.publish(new LogWhiteListEntry(phrase)); 
	}

	@Override
	public EventSource<String> loggedMessages() {
		return _loggedMessages.output();
	}
	
	@Override
	protected String message() {
		String message = super.message();
		for (LogWhiteListEntry logWhiteListEntry: _phrases.output())
			if (message.indexOf(logWhiteListEntry.phrase) != -1)
					return message;
		return "";
	}
	
	@Override
	protected void logMessage() {
		_loggedMessages.notifyReceivers(message());
	}

}
