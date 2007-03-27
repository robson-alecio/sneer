package wheel.io.ui.tests;

import wheel.io.ui.User;

public class TestUser implements User {

	private static final String MY_ANSWER = TestUser.class.getName() + " answer.";

	public void acknowledgeNotification(String notification) {}
	public void acknowledgeUnexpectedProblem(String description) {}

	public String answer(String prompt) { return MY_ANSWER; }
	public String answer(String prompt, String defaultAnswer) { return MY_ANSWER; }

	public boolean choose(String proposition, Object... options) { return true; }

}
