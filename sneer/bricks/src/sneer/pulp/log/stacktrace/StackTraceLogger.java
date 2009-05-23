package sneer.pulp.log.stacktrace;

import sneer.brickness.Brick;

@Brick
public interface StackTraceLogger {

	void logStack();
	String stackToString(Throwable throwable);

}
