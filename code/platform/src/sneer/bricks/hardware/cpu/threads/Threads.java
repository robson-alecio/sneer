package sneer.bricks.hardware.cpu.threads;

import sneer.foundation.brickness.Brick;

@Brick
public interface Threads {

	/** @return a stepper that will call steppable.{@link Steppable#step() step} until the steppable returns false or the stepper itself is garbage collected */
	Stepper newStepper(Steppable steppable);
	
	void startDaemon(String threadName, Runnable runnable);

	void sleepWithoutInterruptions(long milliseconds);
	void waitWithoutInterruptions(Object object);
	void joinWithoutInterruptions(Thread thread);

	void waitUntilCrash();
	void crashAllThreads();
	
	Latch newLatch();

}
