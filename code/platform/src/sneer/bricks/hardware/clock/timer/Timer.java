package sneer.bricks.hardware.clock.timer;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.foundation.brickness.Brick;

@Brick
public interface Timer {

	void sleepAtLeast(long millis);
	WeakContract wakeUpNoEarlierThan(long timeToWakeUp, Runnable runnable);
	WeakContract wakeUpInAtLeast(long millisFromNow, Runnable runnable);
	WeakContract wakeUpEvery(long minimumPeriodInMillis, Steppable stepper);
	WeakContract wakeUpNowAndEvery(long minimumPeriodInMillis, Steppable stepper);

}
