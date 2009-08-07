package sneer.tests.adapters;

import sneer.bricks.hardware.io.log.LogWorker;
import sneer.bricks.hardware.io.log.Logger;

class LoggerForTests implements Logger {

	private final String _prefix;

	
	LoggerForTests(String prefix) {
		_prefix = prefix;
	}

	
	@Override
	public void log(String message, Object... messageInsets) {
		//System.out.println(format(message, messageInsets));
	}

	@Override
	public void setDelegate(LogWorker worker) {
		throw new UnsupportedOperationException();
	}

	String format(String message, Object... messageInsets) {
		StringBuilder result = new StringBuilder();
		
		result.append(_prefix);
		result.append(": ");
		
		formatInsets(result, message, messageInsets);
		
		return result.toString();
	}


	private void formatInsets(StringBuilder builder, String message, Object... messageInsets) {
		String[] parts = message.split("\\{\\}");
		int i = 0;
		while (true) {
			if (i == parts.length) break;
			builder.append(parts[i]);
		
			if (i == messageInsets.length) break;
			builder.append(messageInsets[i]);
			i++;
		}
	}
	
}
