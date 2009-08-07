package sneer.bricks.hardware.io.log.formatter.impl;

import static sneer.foundation.environments.Environments.my;

import java.text.SimpleDateFormat;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.io.log.formatter.LogFormatter;

public class LogFormatterImpl implements LogFormatter {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String format(String message, Object... messageInsets) {
		return logMessage(message, messageInsets).append("\n").toString();
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
		builder.append(SIMPLE_DATE_FORMAT.format(my(Clock.class).time().currentValue()));
	}
}
