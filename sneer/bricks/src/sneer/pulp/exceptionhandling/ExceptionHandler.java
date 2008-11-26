package sneer.pulp.exceptionhandling;

import sneer.kernel.container.Brick;

public interface ExceptionHandler extends Brick {

	void shield(Fallible fallible);

}
