package sneer.bricks.hardware.io.log.exceptions.robust;

import sneer.foundation.brickness.Brick;

@Brick
public interface RobustExceptionLogging {

	void turnOn();
	boolean isOn();
	
}
