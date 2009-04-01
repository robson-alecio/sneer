package sneer.pulp.clock;

import sneer.brickness.OldBrick;
import sneer.pulp.threadpool.Stepper;


public interface Clock extends OldBrick {
	
	long time();
	
	void sleepAtLeast(long millis);

	void wakeUpNoEarlierThan(long timeToWakeUp, Runnable runnable);
	void wakeUpInAtLeast(long millisFromNow, Runnable runnable);
	void wakeUpEvery(long minimumPeriodInMillis, Stepper stepper);
	void wakeUpNowAndEvery(long minimumPeriodInMillis, Stepper stepper);

	void advanceTime(long deltaMillis);
	void advanceTimeTo(long absoluteTimeMillis);

}
