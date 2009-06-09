package sneer.bricks.hardware.cpu.timebox;

import sneer.foundation.brickness.Brick;

@Brick
public interface Timebox {

	/**@param toCallWhenBlocked called when toRun is blocked waiting for a synchronization monitor and cannot be stopped.*/
	void run(int durationInMillis, Runnable toRun, Runnable toCallWhenBlocked);

	Runnable prepare(int durationInMillis, Runnable toRun, Runnable toCallWhenBlocked);


}
