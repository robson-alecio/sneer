package sneer.pulp.exceptionhandling.impl;

import sneer.brickness.environments.Environments;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.logging.Logger;

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