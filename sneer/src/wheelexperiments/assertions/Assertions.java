package wheelexperiments.assertions;

public class Assertions {
	
	public static void assertTrue(boolean condition) {
		if (!condition) throw new IllegalStateException();
	};
	
}
