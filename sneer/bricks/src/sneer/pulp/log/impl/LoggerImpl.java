package sneer.pulp.log.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.log.Logger;
import sneer.pulp.log.Worker;
import sneer.pulp.log.workers.sysout.LogToSystemOut;

class LoggerImpl implements Logger {

	private Worker _delegate = my(LogToSystemOut.class);
	private boolean _shouldLeakThrowables = true;
	
	
	@Override
	public void setDelegate(Worker worker) {
		_delegate = worker;
	}

	@Override
	public void log(String message, Object... messageInsets) {
		_delegate.log(message, messageInsets);
	}

	@Override
	public void log(Throwable throwable, String message, Object... messageInsets) {
		_delegate.log(throwable, message, messageInsets);
		leakIfNecessary(throwable);
	}

	@Override
	public void log(Throwable throwable) {
		_delegate.log(throwable);
		leakIfNecessary(throwable);
	}

	@Override
	public void logShort(Exception e, String message, Object... insets) {
		_delegate.logShort(e, message, insets);
		leakIfNecessary(e);
	}

	@Override
	public void enterRobustMode() {
		_shouldLeakThrowables = false;
	}

	private void leakIfNecessary(Throwable throwable) {
		if (!_shouldLeakThrowables) return;
		throw new RuntimeException("Logger is configured to leak Throwables", throwable);
	}
}