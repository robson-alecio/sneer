package sneer.bricks.hardware.io.log.exceptions;

import sneer.foundation.brickness.Brick;

@Brick
public interface ExceptionLogger {
	
	void log(Throwable throwable);
	void log(Throwable throwable, String message, Object... messageInsets);
	
}
