package wheel.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.output.NullOutputStream;

import wheel.lang.exceptions.NotImplementedYet;

public class Logger {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/** Example: log("User {} is not allowed to access the {} report.", "Peter", "TPS") will produce the following log entry: "User Peter is not allowed to access the TPS report." */
	static public void log(String message, Object... messageInsets) {
		logHeader(inline(message, messageInsets));
		logSeparator();
		flush();
	}

	public static void logShort(Exception e, String message, String... insets) {
		log(inline(message, (Object[])insets) + e.getClass().getSimpleName() + " " + e.getMessage());
	}
	
	/** See log(String, Object...) for examples.*/
	static public void log(Throwable throwable, String message, Object... messageInsets) {
		try {
			if (message == null) message = throwable.getClass().getName();
			logHeader(inline(message, messageInsets));
			throwable.printStackTrace(_log);
			logSeparator();
		} finally {
			flush();
		}
	}
	
	static public void log(Throwable throwable) {
		log(throwable, throwable.getMessage());
	}

	public static void redirectTo(File file) throws FileNotFoundException {
		redirectTo(new FileOutputStream(file, true));
	}

	public static void redirectTo(OutputStream outputStream) {
		log("Redirecting log");
		_log = new PrintWriter(outputStream);
		log("Log redirected to here.");
	}
	
	
	static private PrintWriter _log = new PrintWriter(new NullOutputStream());
	
//	static private PrintWriter _log = new PrintWriter(System.err);

	
	private static void flush() {
		_log.flush();
		
		if (_log.checkError())
			throw new NotImplementedYet();
	}
	
	
	private static void logHeader(String entry) {
		_log.println("" + SIMPLE_DATE_FORMAT.format(new Date()) + "  " + entry);
	}

	
	private static void logSeparator() {
		_log.println();
		_log.flush();
	}

	
	/** Example: inline("User {} is not allowed to access the {} report.", "Peter", "TPS") returns: "User Peter is not allowed to access the TPS report." */
	private static String inline(String message, Object... insets) {
		String result = "";
		String[] parts = message.split("\\{\\}");
		int i = 0;
		while (true) {
			if (i == parts.length) break;
			result += parts[i];
			
			if (i == insets.length) break;
			result += insets[i];
			
			i++;
		}
		return result;
	}


}
