package sneer.bricks.hardware.io.log;

import sneer.foundation.brickness.Brick;

@Brick
public interface Logger extends LogWorker {

	void setDelegate(LogWorker worker);
	
}
