package sneer;

import java.net.URL;

import wheelexperiments.environment.ui.User;
import wheelexperiments.environment.ui.tests.TestUser;

public class TestMode {

	public static boolean isInTestMode() {
		return "true".equals(System.getProperty("sneer.testmode"));
	}

	public static User createUser(URL trayIconURL) {
		return isInTestMode()
			? new TestUser()
			: new User(trayIconURL);
	}

	public static User createUser() {
		return isInTestMode()
			? new TestUser()
			: new User();
	}

}
