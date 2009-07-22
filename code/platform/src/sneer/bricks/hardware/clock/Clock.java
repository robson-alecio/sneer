package sneer.bricks.hardware.clock;

import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.foundation.brickness.Brick;

@Brick
public interface Clock {
	
	long time();
	
	void sleepAtLeast(long millis);

	void wakeUpNoEarlierThan(long timeToWakeUp, Runnable runnable);
	void wakeUpInAtLeast(long millisFromNow, Runnable runnable);
	void wakeUpEvery(long minimumPeriodInMillis, Steppable stepper);
	void wakeUpNowAndEvery(long minimumPeriodInMillis, Steppable stepper);

	void advanceTime(long deltaMillis);
	void advanceTimeTo(long absoluteTimeMillis);

}
