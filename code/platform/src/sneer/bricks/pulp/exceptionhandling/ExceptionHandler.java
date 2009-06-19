package sneer.bricks.pulp.exceptionhandling;

import sneer.foundation.brickness.Brick;

@Brick
public interface ExceptionHandler {

	void shield(Runnable runnable);

}