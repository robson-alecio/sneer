package sneer.pulp.blinkinglights;

import wheel.reactive.lists.ListSignal;

public interface BlinkingLights {

	Light turnOn(LightType type, String message);
	Light turnOn(LightType type, String message, int timeToLive);
	Light turnOn(LightType type, String message, Throwable t);
	Light turnOn(LightType type, String message, Throwable t, int timeToLive);

	void turnOff(Light light);

	ListSignal<Light> lights();

}
