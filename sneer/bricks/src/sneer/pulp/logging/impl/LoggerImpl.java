package sneer.pulp.logging.impl;

import static sneer.commons.environments.Environments.my;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventSource;
import sneer.pulp.logging.LogWhiteListEntry;
import sneer.pulp.tuples.TupleSpace;
import wheel.reactive.impl.EventNotifierImpl;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.impl.ListRegisterImpl;

class LoggerImpl implements sneer.pulp.logging.Logger {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	private final TupleFilterManager _filter = my(TupleFilterManager.class); 	{
		_filter.block(LogWhiteListEntry.class);
	}
	
	private final ListRegisterImpl<LogWhiteListEntry> _phrases = new ListRegisterImpl<LogWhiteListEntry>();
	
	private final EventNotifier<String> _loggedMessages = new EventNotifierImpl<String>();
	private StringWriter _stringWriter = new StringWriter();
	private PrintWriter _log = new PrintWriter(_stringWriter, true);

	private boolean _shouldLeakThrowables = true;	

	LoggerImpl() {
		_tupleSpace.keep(LogWhiteListEntry.class);
		_tupleSpace.addSubscription(LogWhiteListEntry.class, _phrases.adder());
	}
	
	
	@Override
	public ListRegister<LogWhiteListEntry> whiteListEntries() {
		return _phrases;
	}	

//	@Override
//	public Consumer<String> whiteListEntry() {
//		return new Consumer<String>() { @Override public void consume(String phrase) {
//			publish(phrase);
//		}};
//	}
//	
//	private void publish(String phrase) {
//		_tupleSpace.publish(new LogWhiteListEntry(phrase)); 
//	}

	@Override
	public EventSource<String> loggedMessages() {
		return _loggedMessages.output();
	}
	
	private String message() {
		String message = _stringWriter.toString();
		for (LogWhiteListEntry logWhiteListEntry: _phrases.output())
			if (message.indexOf(logWhiteListEntry.phrase) != -1)
					return message;
		return "";
	}
	
	private void logMessage() {
		_loggedMessages.notifyReceivers(message());
	}

	/**
	 * Example: log("User {} is not allowed to access the {} report.", "Peter", "TPS") 
	 * will produce the following log entry:
	 * "User Peter is not allowed to access the TPS report."
	 */
	public synchronized void log(String message, Object... messageInsets) {
		logMessage(inline(message, messageInsets));
		write(null);
	}

	public synchronized void logShort(Exception e, String message, Object... insets) {
		log(inline(message, insets) + " " + e.getClass().getSimpleName() + " " + e.getMessage());
	}

	/** See log(String, Object...) for examples. */
	public synchronized void log(Throwable throwable, String message, Object... messageInsets) {
		 leakIfNecessary(throwable);
		
		if (message == null)
			message = throwable.getClass().getName();
		logMessage(inline(message, messageInsets));
		write(throwable);
	}

	private void leakIfNecessary(Throwable throwable) {
		if (!_shouldLeakThrowables) return;
		throw new RuntimeException("Logger is configured to leak Throwables", throwable);
	}


	public synchronized void log(Throwable throwable) {
		log(throwable, throwable.getMessage());
	}

	private void logThrowable(Throwable throwable) {
		throwable.printStackTrace(_log);
	}

	private void logMessage(String message) {
		_log.println(message);
	}

	private void logHeader(String entry) {
		_log.println(header(entry));
	}

	private String header(String entry) {
		return "" + SIMPLE_DATE_FORMAT.format(new Date()) + "  " + entry;
	}

	private void logSeparator() {
		_log.println();
	}


	private void write(Throwable throwable) {
		String message = message();
	
		if (message.trim().length() > 0)
			logHeader(message);
	
		if (throwable != null)
			logThrowable(throwable);
		
		logSeparator();
		logMessage();
		_stringWriter = new StringWriter();
		_log = new PrintWriter(_stringWriter);
	}


	/**
	 * Example: inline("User {} is not allowed to access the {} report.",
	 * "Peter", "TPS") returns:
	 * "User Peter is not allowed to access the TPS report."
	 */
	private String inline(String message, Object... insets) {
		String result = "";
		String[] parts = message.split("\\{\\}");
		int i = 0;
		while (true) {
			if (i == parts.length)
				break;
			result += parts[i];
	
			if (i == insets.length)
				break;
			result += insets[i];
	
			i++;
		}
		return result;
	}


	@Override
	public void enterRobustMode() {
		_shouldLeakThrowables = false;
	}

}
