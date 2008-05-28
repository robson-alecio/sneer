package functional;

import wheel.reactive.Signal;

public class SignalUtils {

	public static void waitForValue(Object expectedValue, Signal<?> signal) {
		String previousMessage = null;

		while (true) {
			if (expectedValue == null && signal.currentValue() == null) return;
			if (expectedValue != null && expectedValue.equals(signal.currentValue())) return;

			String message = "Expected: " + expectedValue + " Found: " + signal.currentValue();
			if (!message.equals(previousMessage)) {
				previousMessage = message;
				//System.out.println(message);
			}
			Thread.yield();
		}
	}
}
