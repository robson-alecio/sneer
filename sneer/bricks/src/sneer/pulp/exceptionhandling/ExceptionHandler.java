package sneer.pulp.exceptionhandling;

import sneer.kernel.container.Brick;
import wheel.lang.Fallible;
import wheel.lang.exceptions.WheelExceptionHandler;

public interface ExceptionHandler extends WheelExceptionHandler, Brick {

	void shield(Fallible fallible);

}