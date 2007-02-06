package sneer;

import java.net.URL;

import wheel.io.ui.User;
import wheel.io.ui.tests.TestUser;

public class TestMode {

	private int _TODO_Eliminate_this_class_Mock_everything_instead;
	
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
