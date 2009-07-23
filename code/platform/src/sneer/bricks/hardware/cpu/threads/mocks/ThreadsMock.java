package sneer.bricks.hardware.cpu.threads.mocks;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;

public class ThreadsMock implements Threads {

	List<Steppable> _steppers = new ArrayList<Steppable>();
	private List<Runnable> _daemons = new ArrayList<Runnable>();

	@Override
	public synchronized Contract keepStepping(final Steppable stepper) {
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
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}
}