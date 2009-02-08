package wheel.io.logging.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import wheel.io.logging.WheelLogger;

public class WheelLoggerImpl implements WheelLogger {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected StringWriter _stringWriter = new StringWriter();
	protected PrintWriter _log = new PrintWriter(_stringWriter, true);

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
		if (message == null)
			message = throwable.getClass().getName();
		logMessage(inline(message, messageInsets));
		write(throwable);
	}

	public synchronized void log(Throwable throwable) {
		log(throwable, throwable.getMessage());
	}
	
	protected void logThrowable(Throwable throwable) {
		throwable.printStackTrace(_log);
	}
	
	protected void logMessage(String message) {
		_log.println(message);
	}	
	
	protected void logHeader(String entry) {
		_log.println(header(entry));
	}

	protected String header(String entry) {
		return "" + SIMPLE_DATE_FORMAT.format(new Date()) + "  " + entry;
	}

	protected void logSeparator() {
		_log.println();
	}

	protected String message() {
		return _stringWriter.toString();
	}	
	
	protected void write(Throwable throwable) {
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
	
	protected void logMessage() {
		System.out.print(message());
	}	
	
	/**
	 * Example: inline("User {} is not allowed to access the {} report.",
	 * "Peter", "TPS") returns:
	 * "User Peter is not allowed to access the TPS report."
	 */
	protected String inline(String message, Object... insets) {
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

}
