package sneer.pulp.clock;

public interface Clock {
	
	long time();

	void sleep(int millis);

	void addAlarm(int millisFromNow, Runnable runnable);
	void addPeriodicAlarm(int periodInMillis, Runnable runnable);

}
