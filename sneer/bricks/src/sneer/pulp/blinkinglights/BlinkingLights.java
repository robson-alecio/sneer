package sneer.pulp.blinkinglights;

import wheel.lang.exceptions.FriendlyException;
import wheel.reactive.lists.ListSignal;

public interface BlinkingLights {

	Light prepare(LightType type, String caption);

	void turnOnIfNecessary(Light light, FriendlyException e);
	void turnOnIfNecessary(Light light, FriendlyException e, int timeout);
	void turnOnIfNecessary(Light light, Throwable e, String helpMessage, int timeout);

	Light turnOn(LightType type, String message);
	Light turnOn(LightType type, String message, int timeToLive);
	Light turnOn(LightType type, String message, Throwable t);
	Light turnOn(LightType type, String message, Throwable t, int timeToLive);

	void turnOff(Light light);

	ListSignal<Light> lights();





}
