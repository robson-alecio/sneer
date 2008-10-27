package sneer.pulp.threadpool.mocks;

import java.util.ArrayList;
import java.util.List;

import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;

public class ThreadPoolMock implements ThreadPool {

	List<Runnable> _actors = new ArrayList<Runnable>();
	List<Stepper> _steppers = new ArrayList<Stepper>();

	@Override
	public void registerStepper(final Stepper stepper) {
		_steppers.add(stepper);
	}

	@Override
	public void registerActor(Runnable runnable) {
		_actors.add(runnable);
	}

	public void startAllActors(){
		for (Runnable toRun : _actors) toRun.run();
		_actors.clear();
	}

	public Stepper stepper(int i) {
		return _steppers.get(i);
	}

}
