package sneer.bricks.hardware.io.log.exceptions.impl;

import static sneer.foundation.environments.Environments.my;

import java.lang.Thread.UncaughtExceptionHandler;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;

class ExceptionLoggerImpl implements ExceptionLogger, UncaughtExceptionHandler{

	private final Logger _logger = my(Logger.class);
	
	{
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread ignored, Throwable t1) {
		try {
			_logger.log(t1);
		} catch (Throwable t2) {
			t2.printStackTrace();
		}
	}
}