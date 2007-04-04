package wheel.io.ui;

public interface User {

	String answer(String prompt) throws CancelledByUser;
	String answer(String prompt, String defaultAnswer) throws CancelledByUser;
	int answerWithNumber(String prompt) throws CancelledByUser;

	Object choose(String proposition, Object... options) throws CancelledByUser;

	void acknowledgeNotification(String notification);
	void acknowledgeNotification(String notification, String acknowledgement);
	void acknowledgeUnexpectedProblem(String description);

}