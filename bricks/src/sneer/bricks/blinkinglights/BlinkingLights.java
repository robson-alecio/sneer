package sneer.bricks.blinkinglights;

import java.util.List;

public interface BlinkingLights {

	Light turnOn(String message, Throwable t, long timeout /* -1 is never */);

	Light turnOn(String message, Throwable t);
	
	Light turnOn(String message);

	void turnOff(Light light);

	List<Light> listLights();
}
