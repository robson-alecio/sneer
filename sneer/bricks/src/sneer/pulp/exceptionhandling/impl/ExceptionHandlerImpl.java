package sneer.pulp.exceptionhandling.impl;

import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.exceptionhandling.Fallible;
import wheel.io.Logger;

class ExceptionHandlerImpl implements ExceptionHandler {

	@Override
	public void shield(Fallible fallible) {
		try {
			fallible.run();
		} catch (Throwable t) {
			Logger.log(t, "Exception shielded.");
		}
	}

}