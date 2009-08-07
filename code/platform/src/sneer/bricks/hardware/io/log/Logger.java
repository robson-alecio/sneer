package sneer.bricks.hardware.io.log;

import sneer.foundation.brickness.Brick;

@Brick
public interface Logger {

	void log(String message, Object... messageInsets);

	void setDelegate(LogWorker worker);
	
}
