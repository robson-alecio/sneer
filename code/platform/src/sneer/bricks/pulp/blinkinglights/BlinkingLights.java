package sneer.bricks.pulp.blinkinglights;

import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.FriendlyException;

@Brick
public interface BlinkingLights {

	ListSignal<Light> lights();
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

	void askForConfirmation(LightType type, String caption, String helpMessage, Consumer<Boolean> confirmationReceiver);
	
	void turnOffIfNecessary(Light light);


}
