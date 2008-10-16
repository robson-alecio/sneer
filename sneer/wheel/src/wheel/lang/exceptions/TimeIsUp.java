package wheel.lang.exceptions;

public class TimeIsUp extends Error {
	
	public TimeIsUp(StackTraceElement[] stackTrace) {
		super("Timebox ended.");
		setStackTrace(stackTrace);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
