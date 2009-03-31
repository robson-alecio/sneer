package sneer.pulp.threadpool;

import sneer.brickness.Brick;
import sneer.container.NewBrick;

@NewBrick
public interface ThreadPool extends Brick {

	void registerActor(Runnable runnable);
	void registerStepper(Stepper stepper);
}
