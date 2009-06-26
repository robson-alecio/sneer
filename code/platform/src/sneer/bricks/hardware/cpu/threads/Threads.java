package sneer.bricks.hardware.cpu.threads;

import sneer.foundation.brickness.Brick;

@Brick
public interface Threads {

	void registerStepper(Stepper stepper);
	void startDaemon(String threadName, Runnable runnable);

	void sleepWithoutInterruptions(long milliseconds);
	void waitWithoutInterruptions(Object object);
	void joinWithoutInterruptions(Thread thread);

	void waitUntilCrash();
	void crashAllThreads();
	
	Latch newLatch();

}
