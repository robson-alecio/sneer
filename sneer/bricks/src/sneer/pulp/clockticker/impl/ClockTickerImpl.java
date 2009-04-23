package sneer.pulp.clockticker.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.clock.Clock;
import sneer.pulp.clockticker.ClockTicker;
import sneer.pulp.threads.Threads;

class ClockTickerImpl implements ClockTicker {

	private final Threads _threads = my(Threads.class);

	private final Clock _clock = my(Clock.class);

	ClockTickerImpl() {
		tick();
		_threads.registerActor(new Runnable(){ @Override public void run() {
			while (true) tick();
		}});
	}

	private void tick() {
		_clock.advanceTimeTo(System.currentTimeMillis());
		my(Threads.class).sleepWithoutInterruptions(10); //Precision of 100 times per second is OK for now.
	}

}
