package sneer.pulp.blinkinglights.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import wheel.lang.FrozenTime;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class BlinkingLightsImpl implements BlinkingLights {

	private List<Light> _lights = new ArrayList<Light>();
	
	//Fix Make BK reactive
	private static ListRegisterImpl<Light> reg;
	static{
		reg = new ListRegisterImpl<Light>();
		reg.add(new LightImpl("Warning", null, -1));
		
		try {
			String err = null;
			err.toString();
		} catch (NullPointerException e) {
			reg.add(new LightImpl("Error", e, -1));
		}
	}

	@Override
	public void turnOff(Light light) {
		if(light == null) return;
		
		light.turnOff();
		_lights.remove(light);
	}

	@Override
	public Light turnOn(String message, Throwable t, long timeout) {
		Light result = new LightImpl(message, t, timeout); 
		_lights.add(result);
		return result;
	}

	@Override
	public Light turnOn(String message, Throwable t) {
		return turnOn(message, t, LightImpl.NEVER);
	}

	@Override
	public Light turnOn(String message) {
		return turnOn(message, null);
	}

	@Override
	public List<Light> listLights() {
		prune();
		return new ArrayList<Light>(_lights);
	}

	private void prune() {
		CollectionUtils.filter(_lights, new Predicate() {
			@Override
			public boolean evaluate(Object candidate) {
				return ((Light)candidate).isOn();
			}
		});
	}

	@Override
	public ListSignal<Light> lights() {
		return reg.output(); //Fix Make BK reactive

	}
}

class LightImpl implements Light {
	static final int NEVER = -1;

	private String _message;

	private boolean _isOn = true;

	private Throwable _error;

	private final long _expirationTime;

	public LightImpl(String message, Throwable error, long timeout) {
		_message = message;
		_error = error;
		_expirationTime = timeout == NEVER
			? NEVER
			: FrozenTime.frozenTimeMillis() + timeout;
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
		
		
		if (FrozenTime.frozenTimeMillis() >= _expirationTime)
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