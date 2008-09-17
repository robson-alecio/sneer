package sneer.pulp.blinkinglights.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.clock.Clock;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

class BlinkingLightsImpl implements BlinkingLights {
	
	@Inject
	static private Clock _clock;

	private final ListRegisterImpl<Light> _lights = new ListRegisterImpl<Light>();

	@Override
	public Light turnOn(LightType type, String message, Throwable t, int timeout) {
		Light result = new LightImpl(type, message, t); 
		_lights.add(result);
		
		if (timeout != LightImpl.NEVER)
			turnOffIn(result, timeout);
		
		return result;
	}

	@Override
	public Light turnOn(LightType type, String message, Throwable t) {
		return turnOn(type, message, t, LightImpl.NEVER);
	}

	@Override
	public Light turnOn(LightType type, String message) {
		return turnOn(type, message, null);
	}

	@Override
	public ListSignal<Light> lights() {
		return _lights.output();
	}
	
	@Override
	public void turnOff(Light light) {
		if(light == null) return;
		
		if (!_lights.remove(light)) return;
		((LightImpl)light).turnOff();
	}
	
	private void turnOffIn(final Light light, int millisFromNow) {
		_clock.addAlarm(millisFromNow, new Runnable() { @Override public void run() {
			turnOff(light);	
		}});
	}
	
}
