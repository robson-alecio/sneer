package wheel.io.logging.impl;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import wheel.io.logging.WheelLogger;
import wheel.lang.exceptions.NotImplementedYet;

public class WheelLoggerImpl implements WheelLogger {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	private PrintWriter _log = new PrintWriter(System.out);

	/**
	 * Example: log("User {} is not allowed to access the {} report.", "Peter", "TPS") 
	 * will produce the following log entry:
	 * "User Peter is not allowed to access the TPS report."
	 */
	public synchronized void log(String message, Object... messageInsets) {
		logHeader(inline(message, messageInsets));
		logSeparator();
		flush();
	}

	public synchronized void logShort(Exception e, String message, Object... insets) {
		log(inline(message, insets) + " " + e.getClass().getSimpleName() + " " + e.getMessage());
	}

	/** See log(String, Object...) for examples. */
	public synchronized void log(Throwable throwable, String message, Object... messageInsets) {
		try {
			if (message == null)
				message = throwable.getClass().getName();
			logHeader(inline(message, messageInsets));
			logThrowable(throwable);
			logSeparator();
		} finally {
			flush();
		}
	}

	public synchronized void log(Throwable throwable) {
		log(throwable, throwable.getMessage());
	}
	
	protected void flush() {
		_log.flush();
		if (_log.checkError())
			throw new NotImplementedYet(); //TODO			
	}

	protected void logThrowable(Throwable throwable) {
		throwable.printStackTrace(_log);
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
