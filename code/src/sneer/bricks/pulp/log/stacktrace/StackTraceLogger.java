package sneer.bricks.pulp.log.stacktrace;

import sneer.foundation.brickness.Brick;

@Brick
public interface StackTraceLogger {

	void logStack();
	String stackToString(Throwable throwable);

}
