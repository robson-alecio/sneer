package wheel.lang.exceptions.impl;

import wheel.lang.exceptions.WheelExceptionHandler;

public class WheelExceptionLeaker implements WheelExceptionHandler {

	@Override
	public void shield(Runnable runnable) {
		try {
			runnable.run();
		} catch (RuntimeException rx) {
			throw rx;
		} catch (Error e) {
			throw e;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

}
