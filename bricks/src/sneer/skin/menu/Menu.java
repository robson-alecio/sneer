package sneer.skin.menu;

import wheel.io.ui.Action;

public interface Menu<WIDGET> {

	WIDGET getWidget();
	
	void addAction(Action action);
	
	void addGroup(Menu<WIDGET> group);
	
	void addSeparator();
	
	void clearAll();
}