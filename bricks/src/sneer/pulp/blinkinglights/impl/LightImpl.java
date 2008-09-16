package sneer.pulp.blinkinglights.impl;

import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.clock.Clock;

class LightImpl implements Light {
	
	static final int NEVER = 0;
	
	private String _message;
	
	private boolean _isOn = true;
	
	private final Throwable _error;
	
	private final int _timeout;
	
	private final LightType _type;

	public LightImpl(LightType type, String message, Throwable error, int timeout, Clock clock, BlinkingLights blinkingLights) {
		_type = type!=null?type:LightType.INFO;
		_message = message;
		_error = error;
		
		if(timeout == NEVER){
			_timeout =  NEVER;
			return;
		}
		_timeout = timeout;
		addAlarm(clock, blinkingLights);
	}

	private void addAlarm(Clock clock, final BlinkingLights blinkingLights) {
		clock.addAlarm(_timeout, new Runnable() { @Override public void run() {
			blinkingLights.turnOff(LightImpl.this);	
		}});
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
	public void turnOff() {
		_isOn = false;
	}

	@Override
	public LightType type() {
		return _type;
	}
}