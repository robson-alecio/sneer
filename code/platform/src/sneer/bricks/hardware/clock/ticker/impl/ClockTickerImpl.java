package sneer.bricks.hardware.clock.ticker.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.clock.ticker.ClockTicker;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;

class ClockTickerImpl implements ClockTicker {

	private final Threads _threads = my(Threads.class);
	private final Clock _clock = my(Clock.class);
	@SuppressWarnings("unused")	private final Object _refToAvoidGc;

	ClockTickerImpl() {
		tick();
		_refToAvoidGc = _threads.startStepping(new Steppable() { @Override public void step() {
			tick();
		}});
	}

	private void tick() {
		_clock.advanceTimeTo(System.currentTimeMillis());
		_threads.sleepWithoutInterruptions(10); //Precision of 100 times per second is OK for now.
	}

}
