package sneer.pulp.clock.broken;

import sneer.pulp.clock.Clock;

public interface BrokenClock extends Clock {

	void advanceTime(int plusTime);
}