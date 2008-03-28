package sneer.bricks.blinkinglights.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.blinkinglights.BlinkingLights;
import sneer.bricks.blinkinglights.Light;
import sneer.lego.Startable;

public class BlinkingLightsImpl implements BlinkingLights, Startable {

	private List<Light> _lights = new ArrayList<Light>();
	
	@Override
	public void start() throws Exception {
		//TODO: create worked thread and remove old lights
	}

	@Override
	public void turnOff(Light light) {
		if(light == null) return;
		
		light.turnOff();
		_lights.remove(light);
	}

	@Override
	public Light turnOn(String message, Throwable t, long timeout) {
		Light result = new LightImpl(message, t); 
		_lights.add(result);
		return result;
	}

	@Override
	public Light turnOn(String message, Throwable t) {
		return turnOn(message, t, -1);
	}

	@Override
	public Light turnOn(String message) {
		return turnOn(message, null);
	}

	@Override
	public List<Light> listLights() {
		return _lights; //TODO: return safe copy
	}
}

class LightImpl implements Light {

	private String _message;

	private boolean _status;

	private Throwable _error;

	private long _timeout = -1;
	
	public LightImpl(String message, Throwable error) {
		_message = message;
		_error = error;
		_status = true;
	}

	@Override
	public Throwable error() {
		return _error;
	}

	@Override
	public boolean isOn() {
		return _status;
	}

	@Override
	public String message() {
		return _message;
	}

	@Override
	public void turnOff() {
		_status = false;
	}

	@Override
	public long timeout() {
		return _timeout;
	}

	@Override
	public void renew() {
		//TODO: update timeout
		throw new wheel.lang.exceptions.NotImplementedYet();
	}
	
}