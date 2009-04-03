package sneer.pulp.threadpool;

import sneer.brickness.Brick;
import sneer.brickness.OldBrick;

@Brick
public interface ThreadPool extends OldBrick {

	void registerActor(Runnable runnable);
	void registerStepper(Stepper stepper);
}
