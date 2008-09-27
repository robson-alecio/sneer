package sneer.pulp.clockticker.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.clockticker.ClockTicker;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Threads;

public class ClockTickerImpl implements ClockTicker, Runnable {

	@Inject
	private static ThreadPool _threadPool;

	@Inject
	private static Clock _clock;

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
		Threads.sleepWithoutInterruptions(500); //Precision of twice per second is OK for now.
	}

}
