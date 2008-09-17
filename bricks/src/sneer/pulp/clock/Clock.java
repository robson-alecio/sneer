package sneer.pulp.clock;


public interface Clock {
	
	long time();

	void sleep(int millis);

	void addAlarm(int millisFromCurrentTime, Runnable runnable);
	void addPeriodicAlarm(int periodInMillisFromCurrentTime, Runnable runnable);

	void advanceTime(int deltaMillis);
	void advanceTimeTo(long absoluteTimeMillis);

}
