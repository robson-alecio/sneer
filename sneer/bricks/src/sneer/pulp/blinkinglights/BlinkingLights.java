package sneer.pulp.blinkinglights;

import sneer.brickness.Brick;
import wheel.lang.exceptions.FriendlyException;
import wheel.reactive.lists.ListSignal;

public interface BlinkingLights extends Brick {

	Light prepare(LightType type);

	void turnOnIfNecessary(Light light, FriendlyException e);
	void turnOnIfNecessary(Light light, FriendlyException e, int timeout);
	void turnOnIfNecessary(Light light, String caption, Throwable t);
	void turnOnIfNecessary(Light light, String caption, String helpMessage);
	void turnOnIfNecessary(Light light, String caption, String helpMessage, Throwable t);
	void turnOnIfNecessary(Light light, String caption, String helpMessage, Throwable t, int timeout);

	Light turnOn(LightType type, String caption, String helpMessage);
	Light turnOn(LightType type, String caption, String helpMessage, int timeToLive);
	Light turnOn(LightType type, String caption, String helpMessage, Throwable t);
	Light turnOn(LightType type, String caption, String helpMessage, Throwable t, int timeToLive);

	void turnOffIfNecessary(Light light);

	ListSignal<Light> lights();






}
