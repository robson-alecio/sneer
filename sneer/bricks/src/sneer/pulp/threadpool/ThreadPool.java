package sneer.pulp.threadpool;

import sneer.kernel.container.Brick;

public interface ThreadPool extends Brick {

	void registerActor(Runnable runnable);
	void registerStepper(Stepper stepper);
}
