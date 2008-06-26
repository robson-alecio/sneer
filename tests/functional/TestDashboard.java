package functional;


public class TestDashboard {

	public static boolean newTestsShouldRun() {
		return false;
	}

	public static boolean loadTestsShouldRun() {
		boolean result = true;
		if (result) displayLoadTestMessage();
		return result;
	}

	private static void displayLoadTestMessage() {
		if (_wasLoadTestMessageAlreadyDisplayed) return;
		_wasLoadTestMessageAlreadyDisplayed = true;
		
		System.out.println("Load tests are configured to run. You can turn them off here: (TestDashboard.java:1)");
	}
	
	private static boolean _wasLoadTestMessageAlreadyDisplayed = false;
}
