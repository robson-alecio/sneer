package sneer.bricks.hardware.gui.timebox;

import sneer.foundation.brickness.Brick;

@Brick
public interface TimeboxedEventQueue {

	void startQueueing(int timeboxDuration);
	void stopQueueing();

}
