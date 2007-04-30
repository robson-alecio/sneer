package wheel.io.ui.tests;

import wheel.io.ui.User;
import wheel.lang.exceptions.Catcher;
import wheel.lang.exceptions.PrintStackTracer;

public class TestUser implements User {

	private static final String MY_ANSWER = TestUser.class.getName() + " answer.";

	public void acknowledgeNotification(String notification) {}
	public void acknowledgeUnexpectedProblem(String description) {}
	public Catcher catcher() {return new PrintStackTracer(); }
	
	public String answer(String prompt) { return MY_ANSWER; }
	public String answer(String prompt, String defaultAnswer) { return MY_ANSWER; }

	public Object choose(String proposition, Object... options) { return options[0]; }
	public void acknowledgeNotification(String notification, String acknowledgement) {}
	public int answerWithNumber(@SuppressWarnings("unused") String prompt) { return 0; }

}
