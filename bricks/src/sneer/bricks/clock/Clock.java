package sneer.bricks.clock;

public interface Clock {
	long time();

	void sleep(int millis);
}
