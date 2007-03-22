package wheel.io.ui.tests;

import wheel.io.ui.User;

public class TestUser extends User {

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
