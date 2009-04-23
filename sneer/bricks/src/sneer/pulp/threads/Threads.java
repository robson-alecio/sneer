package sneer.pulp.threads;

import sneer.brickness.Brick;

@Brick
public interface Threads {

	void registerActor(Runnable runnable);

	void registerStepper(Stepper stepper);

	void waitWithoutInterruptions(Object object);

	void sleepWithoutInterruptions(long milliseconds);

	void preventFromBeingGarbageCollected(Object reactor);

	void joinWithoutInterruptions(Thread thread);

	ClassLoader contextClassLoader();

	void startDaemon(String threadName, Runnable runnable);

	Runnable createNotifier();

}
