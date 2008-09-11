package sneer.pulp.blinkinglights;

import wheel.reactive.lists.ListSignal;

public interface BlinkingLights {

	Light turnOn(int type, String message, Throwable t, int timeout /* -1 is never */);

	Light turnOn(int type, String message, Throwable t);
	
	Light turnOn(int type, String message);

	void turnOff(Light light);

	ListSignal<Light> lights();

}
