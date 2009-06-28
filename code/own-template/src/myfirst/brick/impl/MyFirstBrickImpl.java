package myfirst.brick.impl;

import myfirst.brick.MyFirstBrick;
import sneer.bricks.hardware.io.log.Logger;
import static sneer.foundation.environments.Environments.my;

public class MyFirstBrickImpl implements MyFirstBrick {

	@Override
	public void doYourThing() {
		my(Logger.class).log("Hello World");
	}

}
