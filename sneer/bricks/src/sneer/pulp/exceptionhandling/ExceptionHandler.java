package sneer.pulp.exceptionhandling;

import sneer.brickness.Brick;

public interface ExceptionHandler extends Brick {

	void shield(Runnable runnable);

}