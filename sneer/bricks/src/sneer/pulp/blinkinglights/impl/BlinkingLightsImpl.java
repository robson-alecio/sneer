package sneer.pulp.blinkinglights.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.clock.Clock;
import wheel.io.Logger;
import wheel.lang.exceptions.FriendlyException;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

class BlinkingLightsImpl implements BlinkingLights {
	
	@Inject
	static private Clock _clock;

	private final ListRegisterImpl<Light> _lights = new ListRegisterImpl<Light>();

	@Override
	public Light turnOn(LightType type, String message, Throwable t, int timeout) {
		Light result = prepare(type, message);
		turnOnIfNecessary(result, t, "Get an expert sovereign friend to help you. ;)", timeout);
		return result;
	}

	@Override
	public Light turnOn(LightType type, String message, Throwable t) {
		return turnOn(type, message, t, LightImpl.NEVER);
	}

	@Override
	public Light turnOn(LightType type, String message, int timeToLive) {
		return turnOn(type, message, null, timeToLive);
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
		if (!light.isOn()) return;
		
		_lights.remove(light);
		Logger.log("Light removed: ", light.caption());
		((LightImpl)light).turnOff();
	}
	
	private void turnOffIn(final Light light, int millisFromNow) {
		_clock.wakeUpInAtLeast(millisFromNow, new Runnable() { @Override public void run() {
			turnOff(light);	
		}});
	}

	@Override
	public Light prepare(LightType type, String caption) {
		return new LightImpl(type, caption);
	}

	@Override
	public void turnOnIfNecessary(Light light, FriendlyException e) {
		turnOnIfNecessary(light, e, LightImpl.NEVER);
	}

	@Override
	public void turnOnIfNecessary(Light light, FriendlyException e, int timeout) {
		turnOnIfNecessary(light, e, e.getHelp(), timeout);
	}

	@Override
	public void turnOnIfNecessary(Light pLight, Throwable e, String helpMessage, int timeout) {
		if (!(pLight instanceof LightImpl)) throw new IllegalArgumentException();
		LightImpl light = (LightImpl)pLight;

		light._isOn = true;
		_lights.add(light);
		
		light._error = e;
		light._helpMessage = helpMessage;
		
		if (timeout != LightImpl.NEVER)
			turnOffIn(light, timeout);
	}


}
