package spikes.wheel.io.ui;

import java.io.File;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.FriendlyException;
import spikes.wheel.lang.exceptions.Catcher;


public interface User {

	String answer(String prompt) throws CancelledByUser;
	String answer(String prompt, String defaultAnswer) throws CancelledByUser;

	Object choose(String proposition, Object... options) throws CancelledByUser;

	boolean confirm(String proposition);
	boolean confirmOrCancel(String proposition) throws CancelledByUser;
	void confirmWithTimeout(String proposition, int timeout, Consumer<Boolean> callback);

	Consumer<Notification> briefNotifier();
	void acknowledgeNotification(String notification);
	void acknowledgeNotification(String notification, String replacementForBoringOK);
	void acknowledgeUnexpectedProblem(String description);
	void acknowledgeUnexpectedProblem(String description, String help);
	void modelessAcknowledge(String title, String message);

	void acknowledge(Throwable t);
	void acknowledgeFriendlyException(FriendlyException e);
	
	void saveAs(String title, String buttonTitle, String[] suffixes, String description, Consumer<File> callback);
	void chooseFolder(String title, String buttonTitle, Consumer<File> callback) ;
	
	Catcher catcher();

	public class Notification {
		public final String _title;
		public final String _notification;

		public Notification(String title, String notification) {
			_title = title;
			_notification = notification;
		}
	}
	

}