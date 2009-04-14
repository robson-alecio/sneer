package wheel.testutil;

import sneer.pulp.reactive.Signal;
import wheel.reactive.sets.SetSignal;

public class SignalUtils {

	public static void waitForValue(Object expectedValue, Signal<?> signal) {
		String previousMessage = null;

		final long t0 = System.currentTimeMillis();
		while (true) {
			if (expectedValue == null && signal.currentValue() == null) return;
			if (expectedValue != null && expectedValue.equals(signal.currentValue())) return;

			String message = "Expected: " + expectedValue + " Found: " + signal.currentValue();
			if (!message.equals(previousMessage)) {
				previousMessage = message;
				//System.out.println(message);
			}
			checkTimeout(t0);
			Thread.yield(); //Optimize
		}
	}

	public static <T> void waitForElement(T expected, SetSignal<T> setSignal) {
		final long t0 = System.currentTimeMillis();
		while (true) {
			if (setSignal.currentContains(expected)) return;
			checkTimeout(t0);
			Thread.yield(); //Optimize
		}
		
	}

	private static void checkTimeout(final long t0) {
		if (System.currentTimeMillis() - t0 > 1000)
			throw new IllegalStateException("timeout");
	}
	
}
