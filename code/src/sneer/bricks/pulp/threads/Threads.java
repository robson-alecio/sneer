package sneer.bricks.pulp.threads;

import sneer.foundation.brickness.Brick;

@Brick
public interface Threads {

	void registerStepper(Stepper stepper);
	void startDaemon(String threadName, Runnable runnable);

	void sleepWithoutInterruptions(long milliseconds);
	void waitWithoutInterruptions(Object object);
	void joinWithoutInterruptions(Thread thread);

	Runnable createNotifier();

	void preventFromBeingGarbageCollected(Object reactor);

	void waitUntilCrash();
	void crashAllThreads();

}
