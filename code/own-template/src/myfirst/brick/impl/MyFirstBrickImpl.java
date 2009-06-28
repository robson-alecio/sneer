package myfirst.brick.impl;

import static sneer.foundation.environments.Environments.my;
import myfirst.brick.MyFirstBrick;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;

public class MyFirstBrickImpl implements MyFirstBrick {

	@Override
	public void helloWorld() {
		my(BlinkingLights.class).turnOn(LightType.GOOD_NEWS, "Hello World", "This Blinking Light was turned on by the demo brick " + MyFirstBrick.class + ".", 15000);
	}

}
