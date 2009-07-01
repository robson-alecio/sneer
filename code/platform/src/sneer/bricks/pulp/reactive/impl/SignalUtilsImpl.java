package sneer.bricks.pulp.reactive.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.SignalUtils;
import sneer.bricks.pulp.reactive.collections.SetSignal;

class SignalUtilsImpl implements SignalUtils {

	@Override
	public void waitForValue(Object expectedValue, Signal<?> signal) {
		while (true) {
			if (expectedValue == null && signal.currentValue() == null) return;
			if (expectedValue != null && expectedValue.equals(signal.currentValue())) return;

			my(Threads.class).sleepWithoutInterruptions(10);
		}
	}

	@Override
	public <T> void waitForElement(T expected, SetSignal<T> setSignal) {
		while (true) {
			if (setSignal.currentContains(expected)) return;
			my(Threads.class).sleepWithoutInterruptions(10);
		}		
	}	
}
