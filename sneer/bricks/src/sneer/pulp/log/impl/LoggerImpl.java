package sneer.pulp.log.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.log.LogWorker;
import sneer.pulp.log.Logger;
import sneer.pulp.log.stacktrace.LogStackTrace;

class LoggerImpl implements Logger {

	private LogWorker _delegate;
	
	@Override
	public void setDelegate(LogWorker worker) {
		_delegate = worker;
	}
	
	@Override
	public void logStackTrace() {
		class StackTrace extends RuntimeException{};
		log(new String(my(LogStackTrace.class).toByteArray(new StackTrace())));
	}
	
	@Override
	public void log(String message, Object... messageInsets) {
		if(_delegate==null) return;
		_delegate.log(message, messageInsets);
	}

	@Override
	public void log(Throwable throwable, String message, Object... messageInsets) {
		leakIfNecessary(throwable);
		_delegate.log(throwable, message, messageInsets);
	}

	@Override
	public void log(Throwable throwable) {
		leakIfNecessary(throwable);
		_delegate.log(throwable);
	}

	@Override
	public void logShort(Throwable throwable, String message, Object... messageInsets) {
		leakIfNecessary(throwable);
		_delegate.logShort(throwable, message, messageInsets);
	}

	private void leakIfNecessary(Throwable throwable) {
		if (_delegate!=null) return;
		throw new RuntimeException("Logger not configured for Throwables (Use Logger.setDelegate()).", throwable);
	}
}