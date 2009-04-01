package sneer.pulp.threadpool;

import sneer.brickness.OldBrick;
import sneer.container.Brick;

@Brick
public interface ThreadPool extends OldBrick {

	void registerActor(Runnable runnable);
	void registerStepper(Stepper stepper);
}
