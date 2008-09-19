package sneer.pulp.clock;


public interface Clock {
	
	long time();

	void sleepAtLeast(int millis);

	void wakeUpNoEarlierThan(long timeToWakeUp, Runnable runnable);
	void wakeUpInAtLeast(int millisFromNow, Runnable runnable);
	void wakeUpEvery(int minimumPeriodInMillis, Runnable runnable);

	void advanceTime(int deltaMillis);
	void advanceTimeTo(long absoluteTimeMillis);

}
