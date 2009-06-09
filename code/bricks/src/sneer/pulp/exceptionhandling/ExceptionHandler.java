package sneer.pulp.exceptionhandling;

import sneer.brickness.Brick;

@Brick
public interface ExceptionHandler {

	void shield(Runnable runnable);

}