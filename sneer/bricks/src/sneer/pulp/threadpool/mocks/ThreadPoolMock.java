package sneer.pulp.threadpool.mocks;

import java.util.ArrayList;
import java.util.List;

import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;

public class ThreadPoolMock implements ThreadPool {

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

	public synchronized void startAllActors(){
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
}