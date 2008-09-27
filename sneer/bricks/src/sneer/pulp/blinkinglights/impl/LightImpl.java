package sneer.pulp.blinkinglights.impl;

import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;

class LightImpl implements Light {
	
	static final int NEVER = 0;
	
	private String _message;
	
	private boolean _isOn = true;
	
	private final Throwable _error;
	
	private final LightType _type;

	public LightImpl(LightType type, String message, Throwable error) {
		_type = type;
		_message = message;
		_error = error;
	}

	@Override
	public Throwable error() {
		return _error;
	}
	
	@Override
	public boolean isOn() {
		return _isOn;
	}
	
	@Override
	public String message() {
		return _message;
	}
	
	@Override
	public LightType type() {
		return _type;
	}

	void turnOff() {
		_isOn = false;
	}
	
}