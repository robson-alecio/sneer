package wheel.lang.exceptions;

import wheel.lang.Fallible;

public interface WheelExceptionHandler {
	
	void shield(Fallible fallible);
	
}
