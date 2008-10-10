package sneer.pulp.blinkinglights.impl;

import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;

class LightImpl implements Light {
	
	static final int NEVER = 0;
	
	boolean _isOn = false;

	private final LightType _type;
	private final String _caption;
	
	Throwable _error;
	String _helpMessage;
	

	public LightImpl(LightType type, String caption) {
		_type = type;
		_caption = caption;
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
	public String caption() {
		return _caption;
	}
	
	@Override
	public LightType type() {
		return _type;
	}

	void turnOff() {
		_isOn = false;
	}

	@Override
	public String helpMessage() {
		return _helpMessage;
	}

}