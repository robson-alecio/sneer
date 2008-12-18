package sneer.pulp.threadpool;

import sneer.kernel.container.Brick;
import wheel.lang.Environments.Memento;

public interface ThreadPool extends Brick {

	void registerActor(Runnable runnable);
	void registerStepper(Stepper stepper);
	
	void dispatch(Memento environment, Runnable runnable);

}
