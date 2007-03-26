package wheel.io.ui;

public interface User {

	String answer(String prompt);
	String answer(String prompt, String defaultAnswer);

	boolean choose(String proposition, Object... options);

	void acknowledgeNotification(String notification);
	void acknowledgeUnexpectedProblem(String description);

}