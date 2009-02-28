package sneer.pulp.threadpool;

import sneer.brickness.Brick;

public interface ThreadPool extends Brick {

	void registerActor(Runnable runnable);
	void registerStepper(Stepper stepper);
}
