package sneer.bricks.hardware.cpu.timebox.impl;

class TimeIsUp extends Error {
	
	public TimeIsUp(StackTraceElement[] stackTrace, String message) {
		super(message);
		setStackTrace(stackTrace);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
