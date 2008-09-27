package wheel.io.ui;

import wheel.io.ui.action.ReactiveAction;


public interface TrayIcon {

	void addAction(ReactiveAction action);
	
	void clearActions();

	void messageBalloon(String title, String message);

	void setDefaultAction(ReactiveAction defaultAction);

}