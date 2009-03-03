package sneer.pulp.exceptionhandling.impl;

import sneer.pulp.exceptionhandling.ExceptionHandler;
import wheel.io.Logger;

class ExceptionHandlerImpl implements ExceptionHandler {

	@Override
	public void shield(Runnable runnable) {
		try {
			runnable.run();
		} catch (Throwable t) {
			Logger.log(t, "Exception shielded.");
		}
	}

}