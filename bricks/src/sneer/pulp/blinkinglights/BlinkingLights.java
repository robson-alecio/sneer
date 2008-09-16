package sneer.pulp.blinkinglights;

import wheel.reactive.lists.ListSignal;

public interface BlinkingLights {

	Light turnOn(LightType type, String message, Throwable t, int timeout /* -1 is never */);

	Light turnOn(LightType type, String message, Throwable t);
	
	Light turnOn(LightType type, String message);

	void turnOff(Light light);

	ListSignal<Light> lights();

}
