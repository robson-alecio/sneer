package sneer.bricks.hardware.io.log.stacktrace.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.hardware.io.log.stacktrace.StackTraceLogger;

class StackTraceLoggerImpl implements StackTraceLogger{

	@Override
	public void logStack() {
		class StackTrace extends RuntimeException{};
		my(Logger.class).log(stackToString(new StackTrace()));
	}

	@Override
	public String stackToString(Throwable throwable) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter _log = new PrintWriter(out, true);
		throwable.printStackTrace(_log);
		return new String(out.toByteArray());
	}
}
