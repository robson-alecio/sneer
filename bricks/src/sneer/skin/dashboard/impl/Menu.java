package sneer.skin.dashboard.impl;

import wheel.io.ui.Action;

public interface Menu {

	void addAction(Action action);
	
	void addGroup(Menu group);
	
	void addSeparator();
	
	void clearAll();
}