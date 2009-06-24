package sneer.bricks.hardware.io.log.stacktrace;

import sneer.foundation.brickness.Brick;

@Brick
public interface StackTraceLogger {

	void logStack();
	String stackToString(Throwable throwable);

}
