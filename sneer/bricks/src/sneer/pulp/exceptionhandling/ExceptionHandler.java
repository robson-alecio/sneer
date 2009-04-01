package sneer.pulp.exceptionhandling;

import sneer.brickness.OldBrick;

public interface ExceptionHandler extends OldBrick {

	void shield(Runnable runnable);

}