package sneer.bricks.hardware.io.log.exceptions.uncaught.impl;

import static sneer.foundation.environments.Environments.my;

import java.lang.Thread.UncaughtExceptionHandler;

import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;
import sneer.bricks.hardware.io.log.exceptions.uncaught.UncaughtExceptionLogger;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

class UncaughtExceptionLoggerImpl implements UncaughtExceptionLogger, UncaughtExceptionHandler {

	private final Environment _environment = my(Environment.class);

	{
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread ignored, final Throwable t1) {
		try {
			Environments.runWith(_environment, new Runnable() {
				@Override
				public void run() {
					my(ExceptionLogger.class).log(t1);
				}
			});
		} catch (Throwable t2) {
			t2.printStackTrace();
			System.err.println("The above was thrown while trying to log this throwable:");
			t1.printStackTrace();
		}
	}
}
