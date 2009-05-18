package sneer.pulp.log.formatter.impl;

import java.text.SimpleDateFormat;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.log.formatter.LogFormatter;
import sneer.pulp.log.stacktrace.LogStackTrace;

public class LogFormatterImpl implements LogFormatter {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final LogStackTrace _logStackTrace = my(LogStackTrace.class);

	@Override
	public String format(String message, Object... messageInsets) {
		return logMessage(message, messageInsets).append("\n").toString();
	}

	@Override
	public String format(Throwable throwable, String message, Object... messageInsets) {
		return logMessage(message, messageInsets)
			.append("\n")
			.append(_logStackTrace.toByteArray(throwable)).append("\n").toString();
	}

	@Override
	public String format(Throwable throwable) {
		return new StringBuilder()
			.append(_logStackTrace.toByteArray(throwable)).append("\n").toString();
	}

	@Override
	public String formatShort(Throwable throwable, String message, Object... messageInsets) {
		return logMessage(message, messageInsets).append(' ')
			.append(throwable.getClass().getSimpleName()).append(' ')
			.append(throwable.getMessage()).append("\n").toString();
	}

	private StringBuilder logMessage(String message, Object... messageInsets) {
		StringBuilder result = new StringBuilder();
		result.append('[');
		appendTime(result);
		result.append("] ");
		format(result, message, messageInsets);
		return result;
	}
	
	private void format(StringBuilder result, String message, Object... insets) {
		String[] parts = message.split("\\{\\}");
		int i = 0;
		while (true) {
			if (i == parts.length) break;
			result.append(parts[i]);
	
			if (i == insets.length) break;
			result.append(insets[i]);
			i++;
		}
	}	
	
	private void appendTime(StringBuilder builder) {
		builder.append(SIMPLE_DATE_FORMAT.format(new java.util.Date()));
	}
}
