package sneer.bricks.hardware.io.log.exceptions.robust.impl;

import sneer.bricks.hardware.io.log.exceptions.robust.RobustExceptionLogging;

class RobustExceptionLoggingImpl implements RobustExceptionLogging {

	private boolean _isOn = false;

	@Override
	public boolean isOn() {
		return _isOn;
	}

	@Override
	public void turnOn() {
		_isOn = true;
	}

	
}
