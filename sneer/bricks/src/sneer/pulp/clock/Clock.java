package sneer.pulp.clock;

import sneer.pulp.threadpool.Stepper;


public interface Clock {
	
	long time();
	
	void sleepAtLeast(int millis);

	void wakeUpNoEarlierThan(long timeToWakeUp, Runnable runnable);
	void wakeUpInAtLeast(int millisFromNow, Runnable runnable);
	void wakeUpEvery(int minimumPeriodInMillis, Stepper stepper);
	void wakeUpNowAndEvery(int minimumPeriodInMillis, Stepper stepper);

	void advanceTime(int deltaMillis);
	void advanceTimeTo(long absoluteTimeMillis);

	//void timebox(int timeoutMillis, Runnable runnable);

}
