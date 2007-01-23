package sneer.strap;

public class TestMode {

	public static boolean isInTestMode() {
		return "true".equals(System.getProperty("sneer.testmode"));
	}

}
