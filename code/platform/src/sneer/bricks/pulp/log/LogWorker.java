package sneer.bricks.pulp.log;

public interface LogWorker {

	void log(String message, Object... messageInsets);
	void logShort(Throwable throwable, String message, Object... messageInsets);
	void log(Throwable throwable, String message, Object... messageInsets) ;
	void log(Throwable throwable);

}
