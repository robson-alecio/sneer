package sneer.bricks.pulp.threads.mocks;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.pulp.threads.Stepper;
import sneer.bricks.pulp.threads.Threads;

public class ThreadsMock implements Threads {

	List<Stepper> _steppers = new ArrayList<Stepper>();

	@Override
	public synchronized void registerStepper(final Stepper stepper) {
		_steppers.add(stepper);
	}

	public synchronized Stepper stepper(int i) {
		return _steppers.get(i);
	}

	public synchronized void stepAllSteppers() {
		ArrayList<Stepper> steppersCopy = new ArrayList<Stepper>(_steppers);
		_steppers.clear();

		for (Stepper stepper : steppersCopy) stepper.step();
	}

	@Override
	public Runnable createNotifier() {
		throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void joinWithoutInterruptions(Thread thread) {
		throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void preventFromBeingGarbageCollected(Object reactor) {
		throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void sleepWithoutInterruptions(long milliseconds) {
		throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void startDaemon(String threadName, Runnable runnable) {
		throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
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
		throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void crashAllThreads() {
		throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
	}
}