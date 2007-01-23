package sneer.strap;

import static sneer.strap.TestMode.isInTestMode;
import wheelexperiments.environment.ui.User;
import wheelexperiments.environment.ui.tests.TestUser;

public class TestMode {

	public static boolean isInTestMode() {
		return "true".equals(System.getProperty("sneer.testmode"));
	}

	public static User createUser() {
		return isInTestMode()
			? new TestUser()
			: new User();
	}

}
