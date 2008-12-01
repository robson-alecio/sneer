package wheel.lang.exceptions.impl;

import wheel.io.Logger;
import wheel.lang.Fallible;
import wheel.lang.exceptions.WheelExceptionHandler;

public class WheelExceptionHandlerImpl implements WheelExceptionHandler {

	@Override
	public void shield(Fallible fallible) {
		try {
			fallible.run();
		} catch (Throwable t) {
			Logger.log(t, "Exception shielded.");
		}
	}

}
