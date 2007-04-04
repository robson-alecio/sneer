package wheel.io.ui.tests;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;

public class TestUser implements User {

	private static final String MY_ANSWER = TestUser.class.getName() + " answer.";

	public void acknowledgeNotification(String notification) {}
	public void acknowledgeUnexpectedProblem(String description) {}

	public String answer(String prompt) { return MY_ANSWER; }
	public String answer(String prompt, String defaultAnswer) { return MY_ANSWER; }

	public Object choose(String proposition, Object... options) { return options[0]; }
	public void acknowledgeNotification(String notification, String acknowledgement) {}
	public int answerWithNumber(String prompt) { return 0; }

}
