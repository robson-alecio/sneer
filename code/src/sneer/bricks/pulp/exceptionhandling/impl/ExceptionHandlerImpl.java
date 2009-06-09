package sneer.bricks.pulp.exceptionhandling.impl;

import sneer.bricks.pulp.exceptionhandling.ExceptionHandler;
import sneer.bricks.pulp.log.Logger;
import sneer.foundation.commons.environments.Environments;

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