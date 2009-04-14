package sneer.hardware.gui.timebox;

import sneer.brickness.Brick;

@Brick
public interface TimeboxedEventQueue {

	void startQueueing(int timeboxDuration);
	void stopQueueing();

}
