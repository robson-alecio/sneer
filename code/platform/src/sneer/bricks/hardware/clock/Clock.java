package sneer.bricks.hardware.clock;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface Clock {
	
	Signal<Long> time();
	void advanceTime(long deltaMillis);
	void advanceTimeTo(long absoluteTimeMillis);

}
