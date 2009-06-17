package sneer.bricks.pulp.log.exceptions.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.lang.Thread.UncaughtExceptionHandler;

import sneer.bricks.pulp.log.Logger;
import sneer.bricks.pulp.log.exceptions.ExceptionLogger;

class ExceptionLoggerImpl implements ExceptionLogger{

	private final Logger _logger = my(Logger.class);
	{
		Thread.setDefaultUncaughtExceptionHandler(
			new UncaughtExceptionHandler(){ @Override public void uncaughtException(Thread ignore, final Throwable throwable) {
				_logger.log(throwable);
			}
		});
	}
}