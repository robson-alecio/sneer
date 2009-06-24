package sneer.bricks.pulp.exceptionhandling.impl;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.pulp.exceptionhandling.ExceptionHandler;
import sneer.foundation.environments.Environments;

class ExceptionHandlerImpl implements ExceptionHandler {

	@Override
	public void shield(Runnable runnable) {
		try {
			runnable.run();
		} catch (Throwable t) {
			Environments.my(Logger.class).log(t, "Exception shielded.");
		}
	}

}