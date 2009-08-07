package sneer.bricks.hardware.io.log.exceptions.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;
import sneer.bricks.hardware.io.log.exceptions.robust.RobustExceptionLogging;
import sneer.bricks.hardware.io.log.stacktrace.StackTraceLogger;


class ExceptionLoggerImpl implements ExceptionLogger {

	@Override
	public void log(Throwable throwable) {
		log(throwable, "{} thrown: {}", throwable.getClass(), throwable.getMessage());
	}

	
	@Override
	public void log(Throwable throwable, String message, Object... messageInsets) {
		leakIfNecessary(throwable);
		message += "/n" + my(StackTraceLogger.class).stackToString(throwable);
		my(Logger.class).log(message, messageInsets);
	}

	
	private void leakIfNecessary(Throwable throwable) {
		if (my(RobustExceptionLogging.class).isOn()) return;
		throw new RuntimeException("Throwable leaked by ExceptionLogger", throwable);
	}
	
}
