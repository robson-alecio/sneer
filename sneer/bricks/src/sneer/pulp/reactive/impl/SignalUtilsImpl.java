package sneer.pulp.reactive.impl;

import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.SignalUtils;
import sneer.pulp.reactive.collections.SetSignal;

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

			System.out.println("Expected: " + expectedValue + " Found: " + signal.currentValue());

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
