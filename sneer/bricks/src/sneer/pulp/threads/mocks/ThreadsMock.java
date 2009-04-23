package sneer.pulp.threads.mocks;

import java.util.ArrayList;
import java.util.List;

import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;

public class ThreadsMock implements Threads {

	List<Runnable> _actors = new ArrayList<Runnable>();
	List<Stepper> _steppers = new ArrayList<Stepper>();

	@Override
	public synchronized void registerStepper(final Stepper stepper) {
		_steppers.add(stepper);
	}

	@Override
	public synchronized void registerActor(Runnable runnable) {
		_actors.add(runnable);
	}

	public synchronized void runAllActors(){
		ArrayList<Runnable> copy = new ArrayList<Runnable>(_actors);
		_actors.clear();
		
		for (Runnable toRun : copy) toRun.run();
	}

	public synchronized Stepper stepper(int i) {
		return _steppers.get(i);
	}

	public List<Runnable> getActors() {
		return _actors;
	}

	@Override
	public ClassLoader contextClassLoader() {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public Runnable createNotifier() {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void joinWithoutInterruptions(Thread thread) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void preventFromBeingGarbageCollected(Object reactor) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void sleepWithoutInterruptions(long milliseconds) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void startDaemon(String threadName, Runnable runnable) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void waitWithoutInterruptions(Object object) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}
}