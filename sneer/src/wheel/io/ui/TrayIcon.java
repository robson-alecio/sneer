package wheel.io.ui;

import sneer.kernel.gui.contacts.ShowContactsScreenAction;


public interface TrayIcon {

	public interface Action {
		String caption();
		void run();
	}

	void addAction(Action action);
	
	void clearActions();

	void seeReminder(String reminder);

	void setDefaultAction(Action defaultAction);

}