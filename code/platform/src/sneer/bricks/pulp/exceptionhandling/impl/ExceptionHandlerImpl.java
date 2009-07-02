package sneer.bricks.pulp.exceptionhandling.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.pulp.exceptionhandling.ExceptionHandler;

class ExceptionHandlerImpl implements ExceptionHandler {

	@Override
	public void shield(Runnable runnable) {
		try {
			runnable.run();
		} catch (Throwable t) {
			my(Logger.class).log(t, "Exception shielded.");
		}
	}

}