package sneer.pulp.blinkinglights;

import wheel.reactive.lists.ListSignal;

public interface BlinkingLights {

	Light turnOn(String message, Throwable t, int timeout /* -1 is never */);

	Light turnOn(String message, Throwable t);
	
	Light turnOn(String message);

	void turnOff(Light light);

	ListSignal<Light> lights();
}
