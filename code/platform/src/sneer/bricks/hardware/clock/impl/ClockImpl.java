package sneer.bricks.hardware.clock.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.pulp.exceptionhandling.ExceptionHandler;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;

class ClockImpl implements Clock {
	
	private final Register<Long> _currentTimeMillis = my(Signals.class).newRegister(0L);
	final ExceptionHandler _exceptionHandler = my(ExceptionHandler.class);
	
	private long currentTime() { return time().currentValue(); }

	@Override public Signal<Long> time() { return _currentTimeMillis.output(); }
	@Override synchronized public void advanceTime(long deltaMillis) { advanceTimeTo(currentTime() + deltaMillis); }
	@Override synchronized public void advanceTimeTo(long absoluteTimeMillis) { _currentTimeMillis.setter().consume(absoluteTimeMillis); }
}