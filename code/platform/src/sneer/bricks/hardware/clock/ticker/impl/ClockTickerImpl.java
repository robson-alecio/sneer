package sneer.bricks.hardware.clock.ticker.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.clock.ticker.ClockTicker;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;

class ClockTickerImpl implements ClockTicker {

	private final Threads _threads = my(Threads.class);

	private final Clock _clock = my(Clock.class);

	private final Steppable _refToAvoidGc;

	ClockTickerImpl() {
		tick();
		_refToAvoidGc = new Steppable() { @Override public boolean step() {
			tick();
			return true;
		}};

		_threads.newStepper(_refToAvoidGc);
	}

	private void tick() {
		_clock.advanceTimeTo(System.currentTimeMillis());
		my(Threads.class).sleepWithoutInterruptions(10); //Precision of 100 times per second is OK for now.
	}

}
