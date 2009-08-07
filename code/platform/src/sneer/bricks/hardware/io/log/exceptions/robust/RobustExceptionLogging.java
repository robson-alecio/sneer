package sneer.bricks.hardware.io.log.exceptions.robust;

import sneer.bricks.software.bricks.snappstarter.Snapp;
import sneer.foundation.brickness.Brick;

@Snapp
@Brick
public interface RobustExceptionLogging {

	void turnOn();
	boolean isOn();
	
}
