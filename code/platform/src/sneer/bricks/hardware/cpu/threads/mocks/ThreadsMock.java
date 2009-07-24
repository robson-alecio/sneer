package sneer.bricks.hardware.cpu.threads.mocks;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.events.Pulser;

public class ThreadsMock implements Threads {

	List<Steppable> _steppers = new ArrayList<Steppable>();
	private List<Runnable> _daemons = new ArrayList<Runnable>();
	private final EventNotifier<Object> _crashingPulser = my(EventNotifiers.class).newInstance();


	@Override
	public synchronized Contract startStepping(final Steppable stepper) {
		_steppers.add(stepper);
		return null;
	}

	public synchronized Steppable stepper(int i) {
		return _steppers.get(i);
	}

	public synchronized void runAllDaemons() {
		ArrayList<Runnable> daemonsCopy = new ArrayList<Runnable>(_daemons);
		_daemons.clear();

		for (Runnable daemon : daemonsCopy) daemon.run();
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
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void startDaemon(String threadName, Runnable runnable) {
		_daemons.add(runnable);
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
	public Pulser crashing() {
		return _crashingPulser.output();
	}
}