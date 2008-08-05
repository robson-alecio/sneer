package functional;

import wheel.reactive.Signal;
import wheel.reactive.sets.SetSignal;

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
			Thread.yield(); //Optimize
		}
	}

	public static <T> void waitForElement(T expected, SetSignal<T> setSignal) {
		System.out.println("Expected: " + expected);

		while (true) {
			if (setSignal.currentElements().contains(expected)) return;
			Thread.yield(); //Optimize
		}
		
	}
}
