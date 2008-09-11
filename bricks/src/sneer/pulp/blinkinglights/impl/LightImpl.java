package sneer.pulp.blinkinglights.impl;

import sneer.pulp.blinkinglights.Light;
import sneer.pulp.clock.Clock;
import wheel.lang.FrozenTime;

public class LightImpl implements Light {
	static final int NEVER = -1;
	
	private String _message;
	
	private boolean _isOn = true;
	
	private Throwable _error;
	
	private final int _expirationTime;

	private final Clock _clock;

	public LightImpl(String message, Throwable error, int timeout, Clock clock) {
		_message = message;
		_error = error;
		_clock = clock;
		_expirationTime = timeout == NEVER ? NEVER : timeout;
	}
	
	@Override
	public Throwable error() {
		return _error;
	}
	
	@Override
	public boolean isOn() {
		checkTimeout();
		return _isOn;
	}
	
	private void checkTimeout() {
		if (!_isOn) return;
		if (_expirationTime == NEVER) return;
		
		_clock.addAlarm(_expirationTime, new Runnable() { @Override public void run() {
			turnOff();	
		}});
		
		if (FrozenTime.frozenTimeMillis() > _expirationTime)
			_isOn = false;
	}
	
	@Override
	public String message() {
		return _message;
	}
	
	@Override
	public void turnOff() {
		_isOn = false;
	}
}