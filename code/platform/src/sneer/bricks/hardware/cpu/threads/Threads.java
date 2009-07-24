package sneer.bricks.hardware.cpu.threads;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.pulp.events.Pulser;
import sneer.foundation.brickness.Brick;

@Brick
public interface Threads {

	Contract startStepping(Steppable steppable);
	
	void startDaemon(String threadName, Runnable runnable);

	void sleepWithoutInterruptions(long milliseconds);
	void waitWithoutInterruptions(Object object);
	void joinWithoutInterruptions(Thread thread);

	void waitUntilCrash();
	void crashAllThreads();
	Pulser crashing();
	
	Latch newLatch();


}
