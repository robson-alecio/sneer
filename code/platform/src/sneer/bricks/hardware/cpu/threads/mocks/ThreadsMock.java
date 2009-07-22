package sneer.bricks.hardware.cpu.threads.mocks;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Stepper;
import sneer.bricks.hardware.cpu.threads.Threads;

public class ThreadsMock implements Threads {

	List<Steppable> _steppers = new ArrayList<Steppable>();

	@Override
	public synchronized Stepper newStepper(final Steppable stepper) {
		_steppers.add(stepper);
		return null;
	}

	public synchronized Steppable stepper(int i) {
		return _steppers.get(i);
	}

	public synchronized void stepAllSteppers() {
		ArrayList<Steppable> steppersCopy = new ArrayList<Steppable>(_steppers);
		_steppers.clear();

		for (Steppable stepper : steppersCopy) stepper.step();
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
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
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