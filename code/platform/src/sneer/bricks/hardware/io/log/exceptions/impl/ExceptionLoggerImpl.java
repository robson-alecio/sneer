package sneer.bricks.hardware.io.log.exceptions.impl;

import static sneer.foundation.environments.Environments.my;

import java.lang.Thread.UncaughtExceptionHandler;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

class ExceptionLoggerImpl implements ExceptionLogger, UncaughtExceptionHandler {

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
					my(Logger.class).log(t1);
				}
			});
		} catch (Throwable t2) {
			t2.printStackTrace();
			System.err.println("The above was thrown while trying to log this throwable:");
			t1.printStackTrace();
		}
	}
}
