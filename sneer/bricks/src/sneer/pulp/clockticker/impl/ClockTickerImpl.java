package sneer.pulp.clockticker.impl;

import sneer.pulp.clock.Clock;
import sneer.pulp.clockticker.ClockTicker;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Threads;
import static sneer.brickness.environments.Environments.my;

class ClockTickerImpl implements ClockTicker, Runnable {

	private final ThreadPool _threadPool = my(ThreadPool.class);

	private final Clock _clock = my(Clock.class);

	ClockTickerImpl() {
		tick();
		_threadPool.registerActor(this);
	}

	@Override
	public void run() {
		while (true) tick();
	}
	
	private void tick() {
		_clock.advanceTimeTo(System.currentTimeMillis());
		Threads.sleepWithoutInterruptions(10); //Precision of 100 times per second is OK for now.
	}

}
