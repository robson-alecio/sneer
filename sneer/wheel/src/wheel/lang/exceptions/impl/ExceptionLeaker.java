package wheel.lang.exceptions.impl;

import wheel.lang.Fallible;
import wheel.lang.exceptions.WheelExceptionHandler;

public class ExceptionLeaker implements WheelExceptionHandler {

	@Override
	public void shield(Fallible fallible) {
		try {
			fallible.run();
		} catch (RuntimeException rx) {
			throw rx;
		} catch (Error e) {
			throw e;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

}
