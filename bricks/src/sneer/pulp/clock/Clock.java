package sneer.pulp.clock;

public interface Clock {
	
	long time();

	void sleep(int millis);

	Alarm setAlarm(int millis, Runnable runnable);
}
