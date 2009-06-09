package sneer.bricks.pulp.reactive.impl;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.SignalUtils;
import sneer.bricks.pulp.reactive.collections.SetSignal;

class SignalUtilsImpl implements SignalUtils {

	private static void checkTimeout(final long t0) {
		if (System.currentTimeMillis() - t0 > 1000)
			throw new IllegalStateException("timeout");
	}

	@Override
	public void waitForValue(Object expectedValue, Signal<?> signal) {
		final long t0 = System.currentTimeMillis();

		while (true) {
			if (expectedValue == null && signal.currentValue() == null) return;
			if (expectedValue != null && expectedValue.equals(signal.currentValue())) return;

			checkTimeout(t0);
			Thread.yield(); //Optimize
		}
	}

	@Override
	public <T> void waitForElement(T expected, SetSignal<T> setSignal) {
		final long t0 = System.currentTimeMillis();

		while (true) {
			if (setSignal.currentContains(expected)) return;
			checkTimeout(t0);
			Thread.yield(); //Optimize
		}		
	}	
}
