package sneer.bricks.hardware.io.log.exceptions.impl;

import static sneer.foundation.environments.Environments.my;

import java.lang.Thread.UncaughtExceptionHandler;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;

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