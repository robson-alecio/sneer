package sneer.pulp.threadpool;

import sneer.brickness.Brick;

@Brick
public interface ThreadPool {

	void registerActor(Runnable runnable);
	void registerStepper(Stepper stepper);
}
