package wheel.io.ui.tests;

import wheel.io.ui.SwingUser;
import wheel.io.ui.TrayIcon.SystemTrayNotSupported;

public class TestUser extends SwingUser {

	public TestUser() throws SystemTrayNotSupported {
		super(null); int todo_clean_this_mess;
	}

	@Override
	public String answer(String prompt, String defaultAnswer) {
		return "Test User Answer";
	}

	@Override
	public boolean choose(String proposition, Object... options) {
		return true;
	}

	@Override
	public void addAction(Action action) {
		System.out.println("Test user does not support actions yet.");
	}

	@Override
	public void acknowledgeNotification(String notification) {
		System.out.println(notification);
	}

	@Override
	public void seeReminder(String reminder) {
		System.out.println(reminder);
	}

}
