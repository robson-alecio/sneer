package wheel.io.ui;

import wheel.io.ui.action.Action;


public interface TrayIcon {

	void addAction(Action action);
	
	void clearActions();

	void messageBalloon(String title, String message);

	void setDefaultAction(Action defaultAction);

}