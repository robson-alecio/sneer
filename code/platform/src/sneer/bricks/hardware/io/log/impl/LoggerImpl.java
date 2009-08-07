package sneer.bricks.hardware.io.log.impl;

import sneer.bricks.hardware.io.log.LogWorker;
import sneer.bricks.hardware.io.log.Logger;

class LoggerImpl implements Logger {

	private LogWorker _delegate;
	
	@Override
	public void setDelegate(LogWorker worker) {
		_delegate = worker;
	}
	
	@Override
	public void log(String message, Object... messageInsets) {
		if (_delegate == null) return;
		_delegate.log(message, messageInsets);
	}

}