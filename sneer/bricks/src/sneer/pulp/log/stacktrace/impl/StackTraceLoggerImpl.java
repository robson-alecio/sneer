package sneer.pulp.log.stacktrace.impl;

import static sneer.commons.environments.Environments.my;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import sneer.pulp.log.Logger;
import sneer.pulp.log.stacktrace.StackTraceLogger;

public class StackTraceLoggerImpl implements StackTraceLogger{

	@Override
	public void logStack() {
		class StackTrace extends RuntimeException{};
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter _log = new PrintWriter(out, true);
		new StackTrace().printStackTrace(_log);
		String result = new String(out.toByteArray());
		
		my(Logger.class).log(result);
	}
}
