package wheel.io.ui;

import wheel.lang.Omnivore;
import wheel.lang.exceptions.Catcher;
import wheel.lang.exceptions.FriendlyException;

public interface User {

	String answer(String prompt) throws CancelledByUser;
	String answer(String prompt, String defaultAnswer) throws CancelledByUser;

	Object choose(String proposition, Object... options) throws CancelledByUser;

	boolean confirm(String proposition);
	boolean confirmOrCancel(String proposition) throws CancelledByUser;
	void confirmWithTimeout(String proposition, int timeout, Omnivore<Boolean> callback);

	void acknowledgeNotification(String notification);
	void acknowledgeNotification(String notification, String replacementForBoringOK);
	void acknowledgeUnexpectedProblem(String description);
	void acknowledgeUnexpectedProblem(String description, String help);

	void acknowledge(Throwable t);
	void acknowledgeFriendlyException(FriendlyException e);
	Catcher catcher();

}