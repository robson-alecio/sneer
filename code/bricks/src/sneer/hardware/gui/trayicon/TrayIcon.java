package sneer.hardware.gui.trayicon;

import sneer.hardware.gui.Action;

public interface TrayIcon {

	void addAction(Action action);
	void setDefaultAction(Action defaultAction);
	void clearActions();

	void messageBalloon(String title, String message);
	
	void dispose();

}
