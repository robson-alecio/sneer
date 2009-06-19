package sneer.bricks.pulp.log;

import sneer.foundation.brickness.Brick;

@Brick
public interface Logger extends LogWorker {

	void setDelegate(LogWorker worker);
	
}
