package sneer.bricks.hardware.clock;

import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface Clock {
	
	Signal<Long> time();
	void advanceTime(long deltaMillis);
	void advanceTimeTo(long absoluteTimeMillis);

	//Refactor Move all methods below to a new Timer brick:
	void sleepAtLeast(long millis);

	void wakeUpNoEarlierThan(long timeToWakeUp, Runnable runnable);
	void wakeUpInAtLeast(long millisFromNow, Runnable runnable);
	void wakeUpEvery(long minimumPeriodInMillis, Steppable stepper);
	void wakeUpNowAndEvery(long minimumPeriodInMillis, Steppable stepper);

}
