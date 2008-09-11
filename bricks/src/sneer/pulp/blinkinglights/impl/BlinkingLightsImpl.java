package sneer.pulp.blinkinglights.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.clock.Clock;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class BlinkingLightsImpl implements BlinkingLights {
	
	@Inject
	static private Clock _clock;	
	
	private final ListRegisterImpl<Light> _lights = new ListRegisterImpl<Light>();
	
	@Override
	public void turnOff(Light light) {
		if(light == null) return;
		
		light.turnOff();
		_lights.remove(light);
	}

	@Override
	public Light turnOn(String message, Throwable t, int timeout) {
		Light result = new LightImpl(message, t, timeout, _clock); 
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
	public ListSignal<Light> lights() {
		return _lights.output();
	}
	
}