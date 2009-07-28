package sneer.bricks.hardware.cpu.threads.mocks;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.events.pulsers.PulseSource;

public class ThreadsMock implements Threads {

	List<Steppable> _steppers = new ArrayList<Steppable>();
	private Map<Runnable, String> _daemonNamesByRunnable = new HashMap<Runnable, String>();
	private final EventNotifier<Object> _crashingPulser = my(EventNotifiers.class).newInstance();


	@Override
	public synchronized Contract startStepping(final Steppable stepper) {
		_steppers.add(stepper);
		return null;
	}

	public synchronized Steppable stepper(int i) {
		return _steppers.get(i);
	}

	public synchronized void runAllDaemonsNamed(String daemonName) {
		Collection<Runnable> daemonsCopy = new ArrayList<Runnable>(_daemonNamesByRunnable.keySet());

		for (Runnable daemon : daemonsCopy) {
			String name = _daemonNamesByRunnable.get(daemon);
			if (!daemonName.equals(name)) continue;
			_daemonNamesByRunnable.remove(daemon);
			daemon.run();
		}
	}

	@Override
	public Latch newLatch() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void joinWithoutInterruptions(Thread thread) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}


	@Override
	public void sleepWithoutInterruptions(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void startDaemon(String threadName, Runnable runnable) {
		_daemonNamesByRunnable.put(runnable, threadName);
	}

	@Override
	public void waitWithoutInterruptions(Object object) {
		try {
			object.wait();

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void waitUntilCrash() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void crashAllThreads() {
		_crashingPulser.notifyReceivers(null);
	}

	@Override
	public PulseSource crashing() {
		return _crashingPulser.output();
	}
}